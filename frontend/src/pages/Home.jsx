import { useEffect, useState } from 'react';
import { Search, SlidersHorizontal, Sparkles } from 'lucide-react';
import api, { errorMessage, unwrap } from '../api/api';
import MovieCard from '../components/MovieCard';
import Alert from '../components/Alert';

export default function Home() {
  const [movies, setMovies] = useState([]);
  const [query, setQuery] = useState('');
  const [status, setStatus] = useState('ALL');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    // Ideally we would debounce the search to the backend, but since the previous implementation 
    // did client-side filtering, we'll keep it simple for now, but expand it to multiple lines.
    const fetchMovies = async () => {
      try {
        const res = await api.get('/movies');
        setMovies(unwrap(res) || []);
      } catch (err) {
        setError(errorMessage(err, 'Unable to load movies.'));
      } finally {
        setLoading(false);
      }
    };
    fetchMovies();
  }, []);

  const filteredMovies = movies.filter(m => {
    const matchesStatus = status === 'ALL' || m.status === status;
    const matchesQuery = m.title.toLowerCase().includes(query.toLowerCase());
    return matchesStatus && matchesQuery;
  });

  return (
    <div className="container">
      <section style={{ padding: '6rem 0', borderBottom: '1px solid var(--border-color)', marginBottom: '4rem' }}>
        <div className="eyebrow"><Sparkles size={14}/> THE CINESPHERE EDIT</div>
        <h1>Pure cinema.<br/><em>Absolute control.</em></h1>
        <p style={{ maxWidth: '500px' }}>
          A clinical, precise environment for the appreciation of film. 
          No distractions. Just the screen and the room.
        </p>
      </section>

      {error && <Alert>{error}</Alert>}

      <section style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', marginBottom: '2rem' }}>
        <div>
          <div className="eyebrow">NOW CURATING</div>
          <h2 style={{ margin: 0 }}>The Collection</h2>
        </div>
        <span style={{ fontSize: '0.875rem', fontWeight: '600' }}>{filteredMovies.length} TITLES</span>
      </section>

      <div style={{ display: 'flex', gap: '1rem', marginBottom: '3rem' }}>
        <div style={{ flex: 1, position: 'relative' }}>
          <Search size={17} style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-tertiary)' }} />
          <input 
            style={{ paddingLeft: '3rem' }}
            placeholder="Search by title..." 
            value={query} 
            onChange={e => setQuery(e.target.value)}
          />
        </div>
        <div style={{ width: '250px', position: 'relative' }}>
          <SlidersHorizontal size={15} style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-tertiary)' }} />
          <select 
            style={{ paddingLeft: '3rem' }}
            value={status} 
            onChange={e => setStatus(e.target.value)}
          >
            <option value="ALL">ALL STATUSES</option>
            <option value="NOW_SHOWING">NOW SHOWING</option>
            <option value="COMING_SOON">COMING SOON</option>
            <option value="ARCHIVED">ARCHIVED</option>
          </select>
        </div>
      </div>

      {loading ? (
        <div className="page-loader"><span className="spinner"/></div>
      ) : filteredMovies.length ? (
        <div className="movie-grid">
          {filteredMovies.map(m => (
            <MovieCard key={m.movieId} movie={m} />
          ))}
        </div>
      ) : (
        <div style={{ textAlign: 'center', padding: '4rem 0', color: 'var(--text-tertiary)' }}>
          <h3>NO MATCHING TITLES</h3>
          <p>Adjust your search parameters.</p>
        </div>
      )}
    </div>
  );
}
