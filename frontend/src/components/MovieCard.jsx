import { useNavigate } from 'react-router-dom';

export default function MovieCard({ movie }) {
  const navigate = useNavigate();

  return (
    <div className="movie-card" onClick={() => navigate(`/movies/${movie.movieId}`)}>
      <div className="movie-poster-wrap">
        <img 
          src={movie.posterUrl || 'https://placehold.co/600x900/f8f9fa/000000?text=CINESPHERE'} 
          alt={movie.title} 
          onError={(e) => { e.target.src = 'https://placehold.co/600x900/f8f9fa/000000?text=CINESPHERE'; }}
        />
      </div>
      <div className="movie-info">
        <h3 className="movie-title">{movie.title}</h3>
        <div className="movie-meta">
          {movie.genre} &bull; {movie.durationMinutes} MIN
        </div>
        <div style={{ fontSize: '0.75rem', fontWeight: '600' }}>
          {movie.status?.replace('_', ' ')}
        </div>
      </div>
    </div>
  );
}
