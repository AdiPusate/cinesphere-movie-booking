import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import api, { errorMessage, unwrap } from '../api/api';
import Alert from '../components/Alert';
import { Ticket } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export default function MyBookings() {
  const { user } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [cancelModal, setCancelModal] = useState(null);
  const nav = useNavigate();

  const handleCancelClick = (bookingId) => {
    setCancelModal(bookingId);
  };

  const confirmCancel = async () => {
    if (!cancelModal) return;
    try {
      await api.post(`/bookings/${cancelModal}/cancel`);
      setBookings(bookings.map(b => b.bookingId === cancelModal ? {...b, bookingStatus: 'CANCELLED'} : b));
    } catch (err) {
      setError(errorMessage(err, 'Failed to cancel booking.'));
    } finally {
      setCancelModal(null);
    }
  };

  useEffect(() => {
    const fetch = async () => {
      try {
        const res = await api.get(`/bookings/user/${user.userId}`);
        setBookings(unwrap(res) || []);
      } catch (err) {
        setError(errorMessage(err, 'Unable to retrieve your bookings.'));
      } finally {
        setLoading(false);
      }
    };
    fetch();
  }, [user]);

  return (
    <div className="container" style={{ maxWidth: '800px' }}>
      <div style={{ marginBottom: '3rem' }}>
        <div className="eyebrow">YOUR RECORD</div>
        <h1 style={{ margin: 0 }}>My Bookings</h1>
      </div>

      {error && <Alert>{error}</Alert>}

      {loading ? (
        <div className="page-loader"><span className="spinner"/></div>
      ) : bookings.length === 0 ? (
        <div style={{ padding: '3rem', textAlign: 'center', background: 'var(--surface-color)' }}>
          <h3>NO BOOKINGS FOUND</h3>
          <p>You have not secured any seats yet.</p>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          {bookings.map(b => (
            <div key={b.bookingId} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '1.5rem', border: '1px solid var(--border-color)' }}>
              <div>
                <h3 style={{ fontSize: '1.25rem', marginBottom: '0.25rem' }}>{b.show?.movie?.title}</h3>
                <div style={{ fontSize: '0.875rem', color: 'var(--text-secondary)', marginBottom: '0.5rem' }}>
                  {b.show?.theatre?.name} &bull; {new Date(b.show?.showTime).toLocaleString()}
                </div>
                <div style={{ display: 'flex', gap: '0.5rem' }}>
                  {b.bookedSeats?.map((seatNum, idx) => (
                    <span key={idx} style={{ fontSize: '0.75rem', fontWeight: '600', padding: '0.2rem 0.5rem', background: 'var(--surface-color)', border: '1px solid var(--border-color)' }}>
                      {seatNum}
                    </span>
                  ))}
                </div>
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', justifyContent: 'space-between' }}>
                <div style={{ fontWeight: '600', fontSize: '1.25rem', marginBottom: '0.5rem' }}>₹{Number(b.totalAmount).toFixed(2)}</div>
                <div style={{ fontSize: '0.75rem', fontWeight: 600, color: b.bookingStatus === 'CANCELLED' ? '#d9534f' : 'inherit' }}>
                  {b.bookingStatus}
                </div>
                <button className="outline-button" onClick={() => nav(`/booking/${b.bookingId}`)} style={{ fontSize: '0.75rem', padding: '0.5rem 1rem' }}>
                  VIEW TICKET <Ticket size={12}/>
                </button>
                {b.bookingStatus === 'CONFIRMED' && (
                  <button className="outline-button" onClick={() => handleCancelClick(b.bookingId)} style={{ fontSize: '0.75rem', padding: '0.5rem 1rem', borderColor: '#d9534f', color: '#d9534f' }}>
                    CANCEL BOOKING
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}

      {cancelModal && (
        <div style={{
          position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
          backgroundColor: 'rgba(0,0,0,0.8)', display: 'flex',
          alignItems: 'center', justifyContent: 'center', zIndex: 1000
        }}>
          <div style={{
            background: 'var(--surface-color)', border: '1px solid var(--border-color)',
            padding: '2rem', maxWidth: '400px', width: '90%', textAlign: 'center'
          }}>
            <h2 style={{marginTop: 0, letterSpacing: '1px', fontWeight: 300, textTransform: 'uppercase'}}>Cancel Booking</h2>
            <p style={{color: 'var(--text-secondary)', marginBottom: '2rem'}}>Are you sure you want to cancel this booking? This action cannot be undone and your seats will be released.</p>
            <div style={{display: 'flex', justifyContent: 'center', gap: '1rem'}}>
              <button className="primary-button" onClick={confirmCancel} style={{background: '#d9534f', borderColor: '#d9534f', color: '#fff'}}>YES, CANCEL</button>
              <button className="outline-button" onClick={() => setCancelModal(null)}>NO, KEEP IT</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
