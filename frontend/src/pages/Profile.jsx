import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import api, { errorMessage, unwrap } from '../api/api';
import Alert from '../components/Alert';

export default function Profile() {
  const { user, setUser } = useAuth();
  const [name, setName] = useState(user?.name || '');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleUpdate = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    try {
      const payload = { name };
      if (password) payload.password = password;

      const res = await api.put('/users/me', payload);
      const updatedUser = unwrap(res);
      setUser(updatedUser);
      setSuccess('Profile updated successfully.');
      setPassword(''); // clear password field
    } catch (err) {
      setError(errorMessage(err, 'Failed to update profile.'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container" style={{ maxWidth: '600px' }}>
      <div style={{ marginBottom: '3rem' }}>
        <div className="eyebrow">YOUR PROFILE</div>
        <h1 style={{ margin: 0 }}>Account Settings</h1>
      </div>

      {error && <Alert>{error}</Alert>}
      {success && <Alert style={{ borderColor: 'var(--success-color)', color: 'var(--success-color)' }}>{success}</Alert>}

      <form onSubmit={handleUpdate} style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem', background: 'var(--surface-color)', padding: '2rem', border: '1px solid var(--border-color)' }}>
        <div>
          <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, marginBottom: '0.5rem' }}>NAME</label>
          <input 
            type="text"
            className="input-field"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
            minLength={2}
          />
        </div>
        <div>
          <label style={{ display: 'block', fontSize: '0.75rem', fontWeight: 600, marginBottom: '0.5rem' }}>NEW PASSWORD (Optional)</label>
          <input 
            type="password"
            className="input-field"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            minLength={6}
            placeholder="Leave blank to keep current password"
          />
        </div>
        <button type="submit" className="primary-button" disabled={loading}>
          {loading ? 'UPDATING...' : 'UPDATE PROFILE'}
        </button>
      </form>
    </div>
  );
}
