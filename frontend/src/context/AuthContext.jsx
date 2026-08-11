import { createContext, useContext, useState, useEffect } from 'react';
import api, { unwrap } from '../api/api';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('cinesphere_token');
    const storedUser = localStorage.getItem('cinesphere_user');
    
    if (token && storedUser && storedUser !== 'undefined') {
      try {
        setUser(JSON.parse(storedUser));
        // Eagerly validate
        api.get('/users/me').then(res => {
          const userData = res?.data?.data ?? res?.data;
          if (userData) {
            setUser(userData);
            localStorage.setItem('cinesphere_user', JSON.stringify(userData));
          }
        }).catch(() => {
          // Interceptor handles 401
        });
      } catch (e) {
        console.error('Failed to parse user from localStorage', e);
      }
    }
    setLoading(false);

    // Setup the interceptor here so we can use navigate
    const interceptor = api.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          localStorage.removeItem('cinesphere_token');
          localStorage.removeItem('cinesphere_user');
          setUser(null);
          navigate('/login');
        }
        return Promise.reject(error);
      }
    );

    return () => {
      api.interceptors.response.eject(interceptor);
    };
  }, [navigate]);

  const login = async (email, password) => {
    const response = await api.post('/auth/login', { email, password });
    const { token, user: userData } = unwrap(response);
    
    localStorage.setItem('cinesphere_token', token);
    localStorage.setItem('cinesphere_user', JSON.stringify(userData));
    setUser(userData);
    return userData;
  };

  const register = async (name, email, password, otpCode) => {
    const response = await api.post('/auth/register', { name, email, password, otpCode });
    const { token, user: userData } = unwrap(response);
    
    localStorage.setItem('cinesphere_token', token);
    localStorage.setItem('cinesphere_user', JSON.stringify(userData));
    setUser(userData);
    return userData;
  };

  const logout = () => {
    localStorage.removeItem('cinesphere_token');
    localStorage.removeItem('cinesphere_user');
    setUser(null);
    navigate('/');
  };

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: !!user, login, register, logout }}>
      {!loading && children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
