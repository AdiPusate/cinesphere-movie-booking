import { useState } from 'react';
import { Link, Navigate, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api, { errorMessage } from '../api/api';
import Alert from '../components/Alert';

function AuthShell({ title, subtitle, children, footer }) {
  return (
    <div style={{ minHeight: '80vh', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
      <div className="auth-panel" style={{ width: '100%' }}>
        <h1 style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>{title}</h1>
        <p style={{ color: 'var(--text-secondary)', marginBottom: '2.5rem' }}>{subtitle}</p>
        {children}
        <div style={{ marginTop: '3rem', paddingTop: '1.5rem', borderTop: '1px solid var(--border-color)', fontSize: '0.875rem', textAlign: 'center' }}>
          {footer}
        </div>
      </div>
    </div>
  );
}

export function Login() {
  const { login, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  if (isAuthenticated) return <Navigate to="/" replace />;

  const submit = async e => {
    e.preventDefault();
    setBusy(true);
    setError('');
    try {
      await login(email, password);
      navigate('/');
    } catch (err) {
      setError(errorMessage(err, 'Access Denied.'));
    } finally {
      setBusy(false);
    }
  };

  return (
    <AuthShell 
      title="AUTHENTICATION" 
      subtitle="Enter your credentials." 
      footer={<>NO CLEARANCE? <Link to="/register" style={{ color: 'var(--text-primary)', fontWeight: '600' }}>REQUEST ACCESS</Link></>}
    >
      {error && <Alert>{error}</Alert>}
      <form onSubmit={submit} className="form-stack">
        <label className="field">EMAIL <input type="email" value={email} onChange={e => setEmail(e.target.value)} required /></label>
        <label className="field">PASSWORD <input type="password" value={password} onChange={e => setPassword(e.target.value)} required /></label>
        
        <div style={{ textAlign: 'right', marginTop: '-0.5rem' }}>
          <Link to="/forgot-password" style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>FORGOT PASSWORD?</Link>
        </div>

        <button className="primary-button" style={{ marginTop: '1rem', width: '100%' }} disabled={busy}>
          {busy ? 'VERIFYING...' : 'LOGIN'}
        </button>
      </form>
    </AuthShell>
  );
}

export function Register() {
  const { register, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  
  const [form, setForm] = useState({ name: '', email: '', password: '', confirm: '', otpCode: '' });
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);
  const [step, setStep] = useState(1);

  if (isAuthenticated) return <Navigate to="/" replace />;

  const change = e => setForm({ ...form, [e.target.name]: e.target.value });
  
  const getOtp = async e => {
    e.preventDefault();
    setError('');
    if (form.password !== form.confirm) return setError('Passwords do not match.');
    setBusy(true);
    try {
      await api.post('/auth/send-otp', { email: form.email, purpose: 'REGISTER' });
      setStep(2);
    } catch (err) {
      setError(errorMessage(err, 'Failed to send OTP.'));
    } finally {
      setBusy(false);
    }
  };

  const submit = async e => {
    e.preventDefault();
    setError('');
    setBusy(true);
    try {
      await register(form.name, form.email, form.password, form.otpCode);
      navigate('/');
    } catch (err) {
      setError(errorMessage(err, 'Registration failed.'));
    } finally {
      setBusy(false);
    }
  };

  return (
    <AuthShell 
      title="REGISTRATION" 
      subtitle="Establish your identity." 
      footer={<>ALREADY CLEARED? <Link to="/login" style={{ color: 'var(--text-primary)', fontWeight: '600' }}>LOGIN</Link></>}
    >
      {error && <Alert>{error}</Alert>}
      
      {step === 1 ? (
        <form onSubmit={getOtp} className="form-stack">
          <label className="field">FULL NAME <input name="name" value={form.name} onChange={change} required /></label>
          <label className="field">EMAIL <input type="email" name="email" value={form.email} onChange={change} required /></label>
          <label className="field">PASSWORD <input type="password" name="password" value={form.password} onChange={change} required /></label>
          <label className="field">CONFIRM PASSWORD <input type="password" name="confirm" value={form.confirm} onChange={change} required /></label>
          <button className="primary-button" style={{ marginTop: '1rem', width: '100%' }} disabled={busy}>
            {busy ? 'PROCESSING...' : 'GET OTP'}
          </button>
        </form>
      ) : (
        <form onSubmit={submit} className="form-stack">
          <p style={{ fontSize: '0.875rem', marginBottom: '1rem' }}>An OTP has been sent to {form.email}. Check backend console.</p>
          <label className="field">OTP CODE <input name="otpCode" value={form.otpCode} onChange={change} required maxLength={6} /></label>
          <button className="primary-button" style={{ marginTop: '1rem', width: '100%' }} disabled={busy}>
            {busy ? 'PROCESSING...' : 'CREATE IDENTITY'}
          </button>
          <button type="button" onClick={() => setStep(1)} className="secondary-button" style={{ width: '100%', marginTop: '0.5rem' }}>BACK</button>
        </form>
      )}
    </AuthShell>
  );
}

export function ForgotPassword() {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [otpCode, setOtpCode] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [step, setStep] = useState(1);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState('');

  const getOtp = async e => {
    e.preventDefault();
    setBusy(true);
    setError('');
    try {
      await api.post('/auth/send-otp', { email, purpose: 'FORGOT_PASSWORD' });
      setStep(2);
    } catch (err) {
      setError(errorMessage(err, 'Failed to send OTP.'));
    } finally {
      setBusy(false);
    }
  };

  const resetPassword = async e => {
    e.preventDefault();
    setBusy(true);
    setError('');
    try {
      await api.post('/auth/forgot-password/reset', { email, otpCode, newPassword });
      navigate('/login');
    } catch (err) {
      setError(errorMessage(err, 'Failed to reset password.'));
    } finally {
      setBusy(false);
    }
  };

  return (
    <AuthShell 
      title="RESET PASSWORD" 
      subtitle="Recover your clearance." 
      footer={<Link to="/login" style={{ color: 'var(--text-primary)', fontWeight: '600' }}>BACK TO LOGIN</Link>}
    >
      {error && <Alert>{error}</Alert>}
      
      {step === 1 ? (
        <form onSubmit={getOtp} className="form-stack">
          <label className="field">EMAIL <input type="email" value={email} onChange={e => setEmail(e.target.value)} required /></label>
          <button className="primary-button" style={{ marginTop: '1rem', width: '100%' }} disabled={busy}>
            {busy ? 'PROCESSING...' : 'GET OTP'}
          </button>
        </form>
      ) : (
        <form onSubmit={resetPassword} className="form-stack">
          <p style={{ fontSize: '0.875rem', marginBottom: '1rem' }}>An OTP has been sent to {email}. Check backend console.</p>
          <label className="field">OTP CODE <input value={otpCode} onChange={e => setOtpCode(e.target.value)} required maxLength={6} /></label>
          <label className="field">NEW PASSWORD <input type="password" value={newPassword} onChange={e => setNewPassword(e.target.value)} required /></label>
          <button className="primary-button" style={{ marginTop: '1rem', width: '100%' }} disabled={busy}>
            {busy ? 'PROCESSING...' : 'CONFIRM RESET'}
          </button>
        </form>
      )}
    </AuthShell>
  );
}
