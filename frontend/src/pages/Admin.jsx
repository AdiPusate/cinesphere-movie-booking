import { useEffect, useState } from 'react';
import api, { unwrap, errorMessage } from '../api/api';
import Alert from '../components/Alert';

export default function Admin() {
  const [coupons, setCoupons] = useState([]);
  const [movies, setMovies] = useState([]);
  const [users, setUsers] = useState([]);
  const [theatres, setTheatres] = useState([]);
  const [shows, setShows] = useState([]);
  
  const [newCoupon, setNewCoupon] = useState({ code: '', discountPercentage: '', expiryDate: '' });
  const [newMovie, setNewMovie] = useState({ title: '', description: '', durationMinutes: '', genre: '', language: '', posterUrl: '', status: 'NOW_SHOWING' });
  const [newTheatre, setNewTheatre] = useState({ name: '', city: '', address: '' });
  const [newShow, setNewShow] = useState({ movieId: '', theatreId: '', showTime: '', basePrice: '' });
  const [seatConfig, setSeatConfig] = useState({ rows: 5, seatsPerRow: 10 });
  const [dashboardStats, setDashboardStats] = useState(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  
  const [activeTab, setActiveTab] = useState('DASHBOARD');
  const [deleteDialog, setDeleteDialog] = useState(null);

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    try {
      const [c, m, u, t, d] = await Promise.all([
        api.get('/coupons'), 
        api.get('/movies'),
        api.get('/users'),
        api.get('/theatres'),
        api.get('/admin/dashboard')
      ]);
      const movieList = unwrap(m) || [];
      const theatreList = unwrap(t) || [];
      setCoupons(unwrap(c) || []);
      setMovies(movieList);
      setUsers(unwrap(u) || []);
      setTheatres(theatreList);
      setDashboardStats(unwrap(d) || null);
      await loadShows(theatreList);
    } catch (e) {
      setError(errorMessage(e, 'Failed to load admin data.'));
    }
  };

  // There is no "get all shows" endpoint, so shows are fetched per theatre and merged.
  const loadShows = async (theatreList) => {
    try {
      const results = await Promise.all(
        theatreList.map(t => api.get(`/shows/theatre/${t.theatreId}`).catch(() => ({ data: { data: [] } })))
      );
      const merged = results.flatMap(r => unwrap(r) || []);
      const byId = new Map(merged.map(s => [s.showId, s]));
      setShows([...byId.values()].sort((a, b) => new Date(a.showTime) - new Date(b.showTime)));
    } catch (e) {
      setError(errorMessage(e, 'Failed to load shows.'));
    }
  };

  const createCoupon = async (e) => {
    e.preventDefault();
    try {
      await api.post('/coupons', { ...newCoupon, discountPercentage: Number(newCoupon.discountPercentage) });
      setNewCoupon({ code: '', discountPercentage: '', expiryDate: '' });
      load();
    } catch (err) {
      setError(errorMessage(err, 'Failed to create coupon.'));
    }
  };

  const createMovie = async (e) => {
    e.preventDefault();
    try {
      await api.post('/movies', { ...newMovie, durationMinutes: Number(newMovie.durationMinutes) });
      setNewMovie({ title: '', description: '', durationMinutes: '', genre: '', language: '', posterUrl: '', status: 'NOW_SHOWING' });
      load();
    } catch (err) {
      setError(errorMessage(err, 'Failed to create movie.'));
    }
  };

  const createTheatre = async (e) => {
    e.preventDefault();
    try {
      await api.post('/theatres', newTheatre);
      setNewTheatre({ name: '', city: '', address: '' });
      load();
    } catch (err) {
      setError(errorMessage(err, 'Failed to create theatre.'));
    }
  };

  const createShow = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      await api.post('/shows', {
        movieId: Number(newShow.movieId),
        theatreId: Number(newShow.theatreId),
        showTime: newShow.showTime, // datetime-local -> LocalDateTime (e.g. 2026-08-15T18:30)
        basePrice: Number(newShow.basePrice)
      });
      setNewShow({ movieId: '', theatreId: '', showTime: '', basePrice: '' });
      setSuccess('Show created. Now generate seats for it below before it appears bookable.');
      load();
    } catch (err) {
      setError(errorMessage(err, 'Failed to create show. Make sure the show time is in the future.'));
    }
  };

  const generateSeats = async (showId) => {
    setError('');
    setSuccess('');
    try {
      await api.post(`/shows/${showId}/seats?rows=${seatConfig.rows}&seatsPerRow=${seatConfig.seatsPerRow}`);
      setSuccess(`Seats generated for show #${showId}.`);
    } catch (err) {
      setError(errorMessage(err, 'Failed to generate seats.'));
    }
  };

  const deleteTheatre = (id) => setDeleteDialog({ type: 'theatre', id });
  const deleteMovie = (id) => setDeleteDialog({ type: 'movie', id });
  const deleteCoupon = (id) => setDeleteDialog({ type: 'coupon', id });
  const deleteUser = (id) => setDeleteDialog({ type: 'user', id });

  const executeDelete = async () => {
    if (!deleteDialog) return;
    const { type, id } = deleteDialog;
    setDeleteDialog(null);
    try {
      if (type === 'movie') await api.delete(`/movies/${id}`);
      if (type === 'theatre') await api.delete(`/theatres/${id}`);
      if (type === 'coupon') await api.delete(`/coupons/${id}`);
      if (type === 'user') await api.delete(`/users/${id}`);
      load();
    } catch (err) {
      setError(errorMessage(err, `Failed to delete ${type}.`));
    }
  };

  const updateUserRole = async (id, role) => {
    try {
      await api.put(`/users/${id}/role?role=${role}`);
      load();
    } catch (err) {
      setError(errorMessage(err, 'Failed to update role.'));
    }
  };

  const tabs = ['DASHBOARD', 'MOVIES', 'THEATRES', 'SHOWS', 'COUPONS', 'USERS'];

  return (
    <div className="container" style={{ maxWidth: '900px' }}>
      <h1 style={{ marginBottom: '2rem' }}>System Administration</h1>
      
      <div style={{ display: 'flex', gap: '1rem', marginBottom: '2rem', borderBottom: '2px solid var(--border-color)' }}>
        {tabs.map(t => (
          <button 
            key={t}
            onClick={() => setActiveTab(t)}
            style={{ 
              background: 'none', 
              border: 'none', 
              padding: '1rem', 
              fontWeight: 600, 
              color: activeTab === t ? 'var(--text-primary)' : 'var(--text-secondary)',
              borderBottom: activeTab === t ? '2px solid var(--text-primary)' : 'none',
              marginBottom: '-2px',
              cursor: 'pointer'
            }}
          >
            {t}
          </button>
        ))}
      </div>

      {error && <Alert>{error}</Alert>}
      {success && <Alert type="success">{success}</Alert>}

      <div style={{ background: 'var(--surface-color)', padding: '2rem', border: '1px solid var(--border-color)' }}>
        
        {activeTab === 'DASHBOARD' && dashboardStats && (
          <section>
            <div className="eyebrow">DASHBOARD</div>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem', marginTop: '1rem' }}>
              <div style={{ padding: '1.5rem', border: '1px solid var(--border-color)', background: 'var(--surface-color)', textAlign: 'center' }}>
                <div style={{ fontSize: '2.5rem', fontWeight: 700, marginBottom: '0.5rem' }}>{dashboardStats.totalMovies}</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', letterSpacing: '0.1em' }}>TOTAL MOVIES</div>
              </div>
              <div style={{ padding: '1.5rem', border: '1px solid var(--border-color)', background: 'var(--surface-color)', textAlign: 'center' }}>
                <div style={{ fontSize: '2.5rem', fontWeight: 700, marginBottom: '0.5rem' }}>{dashboardStats.activeShows}</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', letterSpacing: '0.1em' }}>ACTIVE SHOWS</div>
              </div>
              <div style={{ padding: '1.5rem', border: '1px solid var(--border-color)', background: 'var(--surface-color)', textAlign: 'center' }}>
                <div style={{ fontSize: '2.5rem', fontWeight: 700, marginBottom: '0.5rem' }}>{dashboardStats.totalBookings}</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', letterSpacing: '0.1em' }}>TOTAL BOOKINGS</div>
              </div>
              <div style={{ padding: '1.5rem', border: '1px solid var(--border-color)', background: 'var(--surface-color)', textAlign: 'center' }}>
                <div style={{ fontSize: '2.5rem', fontWeight: 700, marginBottom: '0.5rem' }}>₹{Number(dashboardStats.revenue).toFixed(2)}</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', letterSpacing: '0.1em' }}>TOTAL REVENUE</div>
              </div>
            </div>
          </section>
        )}
        
        {activeTab === 'MOVIES' && (
          <section>
            <form onSubmit={createMovie} className="form-stack" style={{ marginBottom: '3rem' }}>
              <div className="eyebrow">CREATE MOVIE</div>
              <label className="field">TITLE <input value={newMovie.title} onChange={e=>setNewMovie({...newMovie, title: e.target.value})} required /></label>
              <label className="field">POSTER URL <input value={newMovie.posterUrl} onChange={e=>setNewMovie({...newMovie, posterUrl: e.target.value})} /></label>
              <label className="field">SYNOPSIS <textarea rows="3" value={newMovie.description} onChange={e=>setNewMovie({...newMovie, description: e.target.value})} /></label>
              
              <div className="two-col">
                <label className="field">DURATION (MIN) <input type="number" value={newMovie.durationMinutes} onChange={e=>setNewMovie({...newMovie, durationMinutes: e.target.value})} required /></label>
                <label className="field">GENRE <input value={newMovie.genre} onChange={e=>setNewMovie({...newMovie, genre: e.target.value})} /></label>
                <label className="field">LANGUAGE <input value={newMovie.language} onChange={e=>setNewMovie({...newMovie, language: e.target.value})} /></label>
                <label className="field">STATUS 
                  <select value={newMovie.status} onChange={e=>setNewMovie({...newMovie, status: e.target.value})}>
                    <option value="NOW_SHOWING">NOW SHOWING</option>
                    <option value="COMING_SOON">COMING SOON</option>
                    <option value="ARCHIVED">ARCHIVED</option>
                  </select>
                </label>
              </div>
              <button className="primary-button" style={{ alignSelf: 'flex-start' }}>CREATE MOVIE</button>
            </form>

            <div className="eyebrow">MOVIE LIST</div>
            <div style={{ fontSize: '0.875rem' }}>
              {movies.map(m => (
                <div key={m.movieId} style={{ display: 'flex', justifyContent: 'space-between', padding: '1rem 0', borderBottom: '1px solid var(--border-color)', alignItems: 'center' }}>
                  <div>
                    <strong style={{ display: 'block' }}>{m.title}</strong>
                    <span style={{ color: 'var(--text-secondary)' }}>{m.status}</span>
                  </div>
                  <button className="outline-button" onClick={() => deleteMovie(m.movieId)} style={{ padding: '0.5rem 1rem', fontSize: '0.75rem', borderColor: '#d9534f', color: '#d9534f' }}>
                    DELETE
                  </button>
                </div>
              ))}
            </div>
          </section>
        )}

        {activeTab === 'THEATRES' && (
          <section>
            <form onSubmit={createTheatre} className="form-stack" style={{ marginBottom: '3rem' }}>
              <div className="eyebrow">CREATE THEATRE</div>
              <label className="field">NAME <input value={newTheatre.name} onChange={e=>setNewTheatre({...newTheatre, name: e.target.value})} required /></label>
              <div className="two-col">
                <label className="field">CITY <input value={newTheatre.city} onChange={e=>setNewTheatre({...newTheatre, city: e.target.value})} required /></label>
                <label className="field">ADDRESS <input value={newTheatre.address} onChange={e=>setNewTheatre({...newTheatre, address: e.target.value})} required /></label>
              </div>
              <button className="primary-button" style={{ alignSelf: 'flex-start' }}>CREATE THEATRE</button>
            </form>

            <div className="eyebrow">THEATRE LIST</div>
            <div style={{ fontSize: '0.875rem' }}>
              {theatres.map(t => (
                <div key={t.theatreId} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '1rem 0', borderBottom: '1px solid var(--border-color)' }}>
                  <div>
                    <strong style={{ display: 'block' }}>{t.name}</strong>
                    <span style={{ color: 'var(--text-secondary)' }}>{t.city} - {t.address}</span>
                  </div>
                  <button className="outline-button" onClick={() => deleteTheatre(t.theatreId)} style={{ padding: '0.5rem 1rem', fontSize: '0.75rem', borderColor: '#d9534f', color: '#d9534f' }}>
                    DELETE
                  </button>
                </div>
              ))}
            </div>
          </section>
        )}

        {activeTab === 'SHOWS' && (
          <section>
            <form onSubmit={createShow} className="form-stack" style={{ marginBottom: '2rem' }}>
              <div className="eyebrow">CREATE SHOW</div>
              <div className="two-col">
                <label className="field">MOVIE
                  <select value={newShow.movieId} onChange={e=>setNewShow({...newShow, movieId: e.target.value})} required>
                    <option value="" disabled>Select a movie</option>
                    {movies.map(m => (
                      <option key={m.movieId} value={m.movieId}>{m.title}</option>
                    ))}
                  </select>
                </label>
                <label className="field">THEATRE
                  <select value={newShow.theatreId} onChange={e=>setNewShow({...newShow, theatreId: e.target.value})} required>
                    <option value="" disabled>Select a theatre</option>
                    {theatres.map(t => (
                      <option key={t.theatreId} value={t.theatreId}>{t.name} ({t.city})</option>
                    ))}
                  </select>
                </label>
              </div>
              <div className="two-col">
                <label className="field">SHOW TIME <input type="datetime-local" value={newShow.showTime} onChange={e=>setNewShow({...newShow, showTime: e.target.value})} required /></label>
                <label className="field">BASE PRICE (₹) <input type="number" min="1" step="0.01" value={newShow.basePrice} onChange={e=>setNewShow({...newShow, basePrice: e.target.value})} required /></label>
              </div>
              {(!movies.length || !theatres.length) && (
                <p style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>
                  Add at least one movie and one theatre before creating a show.
                </p>
              )}
              <button className="primary-button" style={{ alignSelf: 'flex-start' }} disabled={!movies.length || !theatres.length}>
                CREATE SHOW
              </button>
            </form>

            <div className="eyebrow" style={{ marginBottom: '0.75rem' }}>SEAT GENERATION</div>
            <p style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginBottom: '1rem' }}>
              A show has no bookable seats until you generate them. Set the layout once, then click "GENERATE SEATS" on a show below.
            </p>
            <div className="two-col" style={{ marginBottom: '2rem' }}>
              <label className="field">ROWS <input type="number" min="1" max="26" value={seatConfig.rows} onChange={e=>setSeatConfig({...seatConfig, rows: Number(e.target.value)})} /></label>
              <label className="field">SEATS PER ROW <input type="number" min="1" max="50" value={seatConfig.seatsPerRow} onChange={e=>setSeatConfig({...seatConfig, seatsPerRow: Number(e.target.value)})} /></label>
            </div>

            <div className="eyebrow">SHOW LIST</div>
            <div style={{ fontSize: '0.875rem' }}>
              {shows.length === 0 && <p style={{ color: 'var(--text-secondary)' }}>No shows yet.</p>}
              {shows.map(s => (
                <div key={s.showId} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '1rem 0', borderBottom: '1px solid var(--border-color)' }}>
                  <div>
                    <strong style={{ display: 'block' }}>{s.movie?.title} — {s.theatre?.name}</strong>
                    <span style={{ color: 'var(--text-secondary)' }}>
                      {new Date(s.showTime).toLocaleString()} · ₹{Number(s.basePrice).toFixed(2)}
                    </span>
                  </div>
                  <button
                    className="outline-button"
                    onClick={() => generateSeats(s.showId)}
                    style={{ padding: '0.5rem 1rem', fontSize: '0.75rem' }}
                  >
                    GENERATE SEATS
                  </button>
                </div>
              ))}
            </div>
          </section>
        )}

        {activeTab === 'COUPONS' && (
          <section>
            <form onSubmit={createCoupon} className="form-stack" style={{ marginBottom: '3rem' }}>
              <div className="eyebrow">CREATE COUPON</div>
              <label className="field">CODE <input value={newCoupon.code} onChange={e=>setNewCoupon({...newCoupon, code: e.target.value.toUpperCase()})} required /></label>
              <div className="two-col">
                <label className="field">DISCOUNT % <input type="number" min="1" max="100" value={newCoupon.discountPercentage} onChange={e=>setNewCoupon({...newCoupon, discountPercentage: e.target.value})} required /></label>
                <label className="field">EXPIRY <input type="datetime-local" value={newCoupon.expiryDate} onChange={e=>setNewCoupon({...newCoupon, expiryDate: e.target.value})} required /></label>
              </div>
              <button className="primary-button" style={{ alignSelf: 'flex-start' }}>CREATE COUPON</button>
            </form>

            <div className="eyebrow">COUPON LIST</div>
            <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '0.875rem' }}>
              <thead>
                <tr style={{ borderBottom: '2px solid var(--border-strong)', textAlign: 'left' }}>
                  <th style={{ padding: '0.75rem 0' }}>CODE</th>
                  <th style={{ padding: '0.75rem 0' }}>%</th>
                  <th style={{ padding: '0.75rem 0' }}>EXPIRY</th>
                  <th style={{ padding: '0.75rem 0', textAlign: 'right' }}>ACTIONS</th>
                </tr>
              </thead>
              <tbody>
                {coupons.map(c => (
                  <tr key={c.couponId} style={{ borderBottom: '1px solid var(--border-color)' }}>
                    <td style={{ padding: '0.75rem 0', fontWeight: '600' }}>{c.code}</td>
                    <td style={{ padding: '0.75rem 0' }}>{c.discountPercentage}%</td>
                    <td style={{ padding: '0.75rem 0' }}>{new Date(c.expiryDate).toLocaleString()}</td>
                    <td style={{ padding: '0.75rem 0', textAlign: 'right' }}>
                      <button className="outline-button" onClick={() => deleteCoupon(c.couponId)} style={{ padding: '0.25rem 0.5rem', fontSize: '0.75rem', borderColor: '#d9534f', color: '#d9534f' }}>
                        DELETE
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </section>
        )}

        {activeTab === 'USERS' && (
          <section>
            <div className="eyebrow">USER LIST</div>
            <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '0.875rem', marginTop: '1rem' }}>
              <thead>
                <tr style={{ borderBottom: '2px solid var(--border-strong)', textAlign: 'left' }}>
                  <th style={{ padding: '0.75rem 0' }}>NAME</th>
                  <th style={{ padding: '0.75rem 0' }}>EMAIL</th>
                  <th style={{ padding: '0.75rem 0' }}>ROLE</th>
                </tr>
              </thead>
              <tbody>
                {users.map(u => (
                  <tr key={u.userId} style={{ borderBottom: '1px solid var(--border-color)' }}>
                    <td style={{ padding: '0.75rem 0', fontWeight: '600' }}>{u.name}</td>
                    <td style={{ padding: '0.75rem 0' }}>{u.email}</td>
                    <td style={{ padding: '0.75rem 0' }}>
                      <select 
                        value={u.role} 
                        onChange={(e) => updateUserRole(u.userId, e.target.value)}
                        style={{ padding: '0.25rem', fontSize: '0.75rem', fontFamily: 'inherit', marginRight: '1rem' }}
                      >
                        <option value="CUSTOMER">CUSTOMER</option>
                        <option value="ADMIN">ADMIN</option>
                      </select>
                      <button className="outline-button" onClick={() => deleteUser(u.userId)} style={{ padding: '0.25rem 0.5rem', fontSize: '0.75rem', borderColor: '#d9534f', color: '#d9534f' }}>
                        DELETE
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </section>
        )}

      </div>

      {deleteDialog && (
        <div style={{
          position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
          backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000
        }}>
          <div style={{ background: 'var(--surface-color)', padding: '2rem', border: '1px solid var(--border-color)', maxWidth: '400px', width: '100%' }}>
            <h2 style={{ marginTop: 0 }}>Confirm Deletion</h2>
            <p>Are you sure you want to delete this {deleteDialog.type}? This action cannot be undone.</p>
            <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem' }}>
              <button className="primary-button" onClick={executeDelete} style={{ flex: 1, backgroundColor: '#d9534f', color: '#fff' }}>DELETE</button>
              <button className="outline-button" onClick={() => setDeleteDialog(null)} style={{ flex: 1 }}>CANCEL</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
