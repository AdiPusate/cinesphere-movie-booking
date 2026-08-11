import { useEffect, useState } from 'react';
import { ArrowLeft, Clock, MapPin, Ticket } from 'lucide-react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import api, { errorMessage, unwrap } from '../api/api';
import Alert from '../components/Alert';

export default function MovieDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [movie, setMovie] = useState(null);
  const [shows, setShows] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [movieRes, showsRes] = await Promise.all([
          api.get(`/movies/${id}`),
          api.get(`/shows/movie/${id}`)
        ]);
        setMovie(unwrap(movieRes));
        setShows(unwrap(showsRes) || []);
      } catch (err) {
        setError(errorMessage(err, 'Unable to load this title.'));
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [id]);

  if (loading) return <div className="page-loader"><span className="spinner"/></div>;
  
  if (error || !movie) {
    return (
      <div className="container" style={{ maxWidth: '600px' }}>
        <Alert>{error || 'Movie not found.'}</Alert>
        <Link to="/" style={{ color: 'var(--text-primary)', fontWeight: '600' }}>RETURN TO CATALOGUE</Link>
      </div>
    );
  }

  const groupedShows = shows.reduce((acc, show) => {
    const tId = show.theatre?.theatreId || 'unknown';
    if (!acc[tId]) {
      acc[tId] = { theatre: show.theatre || { name: 'Unknown Theatre' }, shows: [] };
    }
    acc[tId].shows.push(show);
    return acc;
  }, {});

  return (
    <div className="container">
      <button className="outline-button" onClick={() => navigate(-1)} style={{ marginBottom: '3rem' }}>
        <ArrowLeft size={15} /> BACK
      </button>

      <section style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: '4rem', marginBottom: '4rem' }}>
        <div style={{ aspectRatio: '2/3', background: 'var(--surface-color)' }}>
          <img 
            src={movie.posterUrl || 'https://placehold.co/600x900/f8f9fa/000000?text=CINESPHERE'} 
            alt={movie.title}
            onError={(e) => { e.target.src = 'https://placehold.co/600x900/f8f9fa/000000?text=CINESPHERE'; }}
            style={{ width: '100%', height: '100%', objectFit: 'cover', filter: 'grayscale(10%) contrast(110%)' }}
          />
        </div>
        <div>
          <div className="eyebrow">{movie.status?.replaceAll('_', ' ')}</div>
          <h1 style={{ fontSize: '4rem', marginBottom: '1rem' }}>{movie.title}</h1>
          <div style={{ display: 'flex', gap: '1.5rem', marginBottom: '2rem', fontSize: '0.875rem', fontWeight: '600', color: 'var(--text-secondary)' }}>
            <span style={{ display: 'flex', alignItems: 'center', gap: '0.25rem' }}><Clock size={15}/> {movie.durationMinutes} MIN</span>
            <span>{movie.genre}</span>
            <span>{movie.language}</span>
            {movie.rating != null && <span>RATING: {Number(movie.rating).toFixed(1)}/10</span>}
          </div>
          <p style={{ fontSize: '1.125rem', maxWidth: '600px', lineHeight: '1.6' }}>
            {movie.description || 'No synopsis provided.'}
          </p>
        </div>
      </section>

      <section>
        <div style={{ borderBottom: '1px solid var(--border-color)', paddingBottom: '1rem', marginBottom: '2rem' }}>
          <div className="eyebrow">SCREENINGS</div>
          <h2 style={{ margin: 0 }}>Available Sessions</h2>
        </div>

        {shows.length === 0 ? (
          <div style={{ padding: '3rem', background: 'var(--surface-color)', textAlign: 'center' }}>
            <h3 style={{ margin: 0 }}>NO SESSIONS SCHEDULED</h3>
            <p style={{ margin: '0.5rem 0 0 0' }}>Please check back later.</p>
          </div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '2.5rem' }}>
            {Object.values(groupedShows).map(group => (
              <div key={group.theatre.theatreId} style={{ border: '1px solid var(--border-color)' }}>
                <div style={{ padding: '1.5rem', background: 'var(--surface-color)', borderBottom: '1px solid var(--border-color)' }}>
                  <strong style={{ fontSize: '1.25rem', display: 'block', marginBottom: '0.25rem' }}>{group.theatre.name}</strong>
                  {group.theatre.city && (
                    <span style={{ display: 'flex', alignItems: 'center', gap: '0.25rem', color: 'var(--text-secondary)', fontSize: '0.875rem' }}>
                      <MapPin size={13}/> 
                      {group.theatre.city} - {group.theatre.address}
                    </span>
                  )}
                </div>
                <div style={{ padding: '1.5rem', display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))', gap: '1rem' }}>
                  {group.shows.map(show => (
                    <div key={show.showId} style={{ padding: '1rem', border: '1px solid var(--border-color)', display: 'flex', flexDirection: 'column', justifyContent: 'space-between', gap: '1rem' }}>
                      <div style={{ fontWeight: 600, fontSize: '1.125rem' }}>
                        {new Date(show.showTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                        <div style={{ fontSize: '0.875rem', color: 'var(--text-secondary)', marginTop: '0.25rem' }}>
                          {new Date(show.showTime).toLocaleDateString()}
                        </div>
                      </div>
                      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <span style={{ fontWeight: '600' }}>₹{Number(show.basePrice).toFixed(2)}</span>
                        <button className="primary-button" onClick={() => navigate(`/shows/${show.showId}/seats`)} style={{ padding: '0.5rem 1rem', fontSize: '0.75rem' }}>
                          BOOK SEAT
                        </button>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}
