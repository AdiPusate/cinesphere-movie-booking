import { Link, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Ticket } from 'lucide-react';

export default function Layout() {
  const { isAuthenticated, user, logout } = useAuth();

  return (
    <>
      <nav className="navbar">
        <Link to="/" className="brand">
          <Ticket size={24} />
          CINESPHERE
        </Link>
        <div className="nav-links">
          {isAuthenticated ? (
            <>
              <span className="nav-link" style={{ fontWeight: 'bold', color: 'var(--text-primary)', marginRight: '1rem' }}>
                [{user?.name}]
              </span>
              {user?.role === 'ADMIN' && (
                <Link to="/admin" className="nav-link">Admin</Link>
              )}
              <Link to="/my-bookings" className="nav-link">Bookings</Link>
              <Link to="/profile" className="nav-link">Profile</Link>
              <button onClick={logout} className="nav-link" style={{ textTransform: 'uppercase' }}>
                Logout
              </button>
            </>
          ) : (
            <Link to="/login" className="nav-link">Login</Link>
          )}
        </div>
      </nav>
      <main>
        <Outlet />
      </main>
    </>
  );
}
