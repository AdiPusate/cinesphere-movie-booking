import { useEffect, useMemo, useState } from 'react';
import { ArrowLeft, Check, Lock, Tag, Ticket } from 'lucide-react';
import { useNavigate, useParams } from 'react-router-dom';
import api, { errorMessage, unwrap } from '../api/api';
import Alert from '../components/Alert';
import { useAuth } from '../context/AuthContext';

export default function SeatSelection() {
  const { showId } = useParams();
  const nav = useNavigate();
  const { isAuthenticated, user } = useAuth();
  
  const [show, setShow] = useState(null);
  const [seats, setSeats] = useState([]);
  const [selected, setSelected] = useState([]);
  const [coupon, setCoupon] = useState('');
  const [couponData, setCouponData] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);
  const [busy, setBusy] = useState(false);

  useEffect(() => {
    const loadSeats = async () => {
      try {
        const [showRes, seatsRes] = await Promise.all([
          api.get(`/shows/${showId}`),
          api.get(`/shows/${showId}/seats`)
        ]);
        setShow(unwrap(showRes));
        setSeats(unwrap(seatsRes) || []);
      } catch (e) {
        setError(errorMessage(e, 'Unable to load seating arrangement.'));
      } finally {
        setLoading(false);
      }
    };
    loadSeats();
  }, [showId]);

  const rows = useMemo(() => {
    const map = {};
    seats.forEach(s => {
      const r = (s.seatNumber.match(/^[A-Z]+/) || ['A'])[0];
      if (!map[r]) map[r] = [];
      map[r].push(s);
    });
    return map;
  }, [seats]);

  const toggleSeat = (s) => {
    if (s.status !== 'AVAILABLE') return;
    setError('');
    setSelected(prev => {
      if (prev.some(x => x.seatId === s.seatId)) {
        return prev.filter(x => x.seatId !== s.seatId);
      }
      if (prev.length >= 10) return prev;
      return [...prev, s];
    });
  };

  const subtotal = (show?.basePrice || 0) * selected.length;
  const total = couponData ? subtotal - (subtotal * couponData.discountPercentage / 100) : subtotal;

  const applyCoupon = async () => {
    if (!coupon.trim()) return;
    try {
      const code = encodeURIComponent(coupon.trim().toUpperCase());
      const res = await api.get(`/coupons/validate/${code}`);
      setCouponData(unwrap(res));
      setError('');
    } catch (e) {
      setCouponData(null);
      setError(errorMessage(e, 'Invalid coupon code.'));
    }
  };

  const confirmBooking = async () => {
    if (!isAuthenticated) return nav('/login');
    if (!selected.length) {
      setError('Please select at least one seat.');
      return;
    }
    
    setBusy(true);
    setError('');
    try {
      const res = await api.post(`/bookings/user/${user.userId}`, {
        showId: Number(showId),
        seatNumbers: selected.map(s => s.seatNumber),
        couponCode: couponData?.code || null
      });
      const booking = unwrap(res);
      nav(`/booking/${booking.bookingId}`);
    } catch (e) {
      setError(errorMessage(e, 'Booking failed. Seats may no longer be available.'));
      // Refresh seats
      try {
        const fresh = unwrap(await api.get(`/shows/${showId}/seats`));
        if (fresh && fresh.length) setSeats(fresh);
      } catch (err) {
        // Ignore secondary error
      }
    } finally {
      setBusy(false);
    }
  };

  if (loading) return <div className="page-loader"><span className="spinner"/></div>;

  return (
    <div className="container" style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: '4rem' }}>
      
      <section>
        <button className="outline-button" onClick={() => nav(-1)} style={{ marginBottom: '2rem' }}>
          <ArrowLeft size={15}/> BACK
        </button>

        <div style={{ marginBottom: '3rem' }}>
          <div className="eyebrow">{show?.movie?.title}</div>
          <h2 style={{ margin: 0 }}>Select your seats</h2>
        </div>

        {error && <Alert>{error}</Alert>}

        <div className="seat-panel">
          <div className="screen-line"><span>SCREEN</span></div>
          
          <div className="seat-map">
            {Object.entries(rows)
              .sort(([a], [b]) => a.localeCompare(b))
              .map(([row, list]) => (
                <div className="seat-row" key={row}>
                  <span className="row-label">{row}</span>
                  {list
                    .sort((a, b) => a.seatNumber.localeCompare(b.seatNumber, undefined, { numeric: true }))
                    .map(s => {
                      const isSelected = selected.some(x => x.seatId === s.seatId);
                      return (
                        <button 
                          key={s.seatId} 
                          disabled={s.status !== 'AVAILABLE'} 
                          className={`seat ${s.status.toLowerCase()} ${isSelected ? 'selected' : ''}`} 
                          onClick={() => toggleSeat(s)} 
                          title={`${s.seatNumber}: ${s.status}`}
                        >
                          {isSelected ? <Check size={13}/> : s.seatNumber.replace(row, '')}
                        </button>
                      );
                    })}
                </div>
            ))}
          </div>

          <div style={{ display: 'flex', justifyContent: 'center', gap: '2rem', marginTop: '3rem', fontSize: '0.75rem', fontWeight: '600' }}>
            <span style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <div style={{ width: '16px', height: '16px', border: '1px solid var(--border-color)' }} /> AVAILABLE
            </span>
            <span style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <div style={{ width: '16px', height: '16px', background: 'var(--accent-color)' }} /> SELECTED
            </span>
            <span style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <div style={{ width: '16px', height: '16px', background: 'var(--surface-hover)' }} /> UNAVAILABLE
            </span>
          </div>
        </div>
      </section>

      <aside>
        <div style={{ border: '1px solid var(--border-color)', padding: '2rem', position: 'sticky', top: '2rem' }}>
          <div className="eyebrow" style={{ marginBottom: '1.5rem' }}>TRANSACTION SUMMARY</div>
          
          <div style={{ marginBottom: '2rem', paddingBottom: '2rem', borderBottom: '1px solid var(--border-color)' }}>
            <h3 style={{ fontSize: '1.25rem', marginBottom: '0.5rem' }}>{show?.movie?.title}</h3>
            <div style={{ fontSize: '0.875rem', color: 'var(--text-secondary)' }}>
              {show?.theatre?.name} &bull; {new Date(show?.showTime).toLocaleString()}
            </div>
          </div>

          <div style={{ marginBottom: '2rem' }}>
            <label className="field">SEATS SELECTED</label>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.5rem' }}>
              {selected.length ? selected.map(s => (
                <span key={s.seatId} style={{ padding: '0.25rem 0.5rem', background: 'var(--surface-color)', fontSize: '0.875rem', fontWeight: '600', border: '1px solid var(--border-color)' }}>
                  {s.seatNumber}
                </span>
              )) : <span style={{ color: 'var(--text-tertiary)', fontStyle: 'italic', fontSize: '0.875rem' }}>None selected</span>}
            </div>
          </div>

          <div style={{ marginBottom: '2rem', paddingBottom: '2rem', borderBottom: '1px solid var(--border-color)' }}>
            <label className="field"><Tag size={14}/> COUPON CODE</label>
            <div style={{ display: 'flex', gap: '0.5rem' }}>
              <input 
                value={coupon} 
                onChange={e => setCoupon(e.target.value)} 
                disabled={Boolean(couponData)} 
                placeholder="e.g. VIP20"
              />
              <button className="outline-button" onClick={applyCoupon} disabled={Boolean(couponData) || !coupon}>
                APPLY
              </button>
            </div>
            {couponData && <div style={{ fontSize: '0.75rem', fontWeight: '600', marginTop: '0.5rem', color: 'var(--success-color)' }}>COUPON APPLIED: -{couponData.discountPercentage}%</div>}
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', marginBottom: '2rem' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.5rem' }}>
              <span>Subtotal</span>
              <strong>₹{subtotal.toFixed(2)}</strong>
            </div>
            {couponData && (
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.875rem', color: 'var(--success-color)' }}>
                <span>DISCOUNT</span>
                <strong>-₹{(subtotal - total).toFixed(2)}</strong>
              </div>
            )}
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '1.5rem', fontWeight: 'bold', marginTop: '1rem', borderTop: '2px solid var(--border-color)', paddingTop: '1rem' }}>
              <span>Total</span>
              <span>₹{total.toFixed(2)}</span>
            </div>
          </div>

          <button 
            className="primary-button" 
            style={{ width: '100%', justifyContent: 'center' }}
            disabled={busy || !selected.length} 
            onClick={confirmBooking}
          >
            {busy ? 'PROCESSING...' : <>AUTHORIZE PAYMENT <Ticket size={15}/></>}
          </button>
          
          {!isAuthenticated && (
            <p style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', marginTop: '1rem', textAlign: 'center', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '0.25rem' }}>
              <Lock size={12}/> YOU WILL BE PROMPTED TO SIGN IN
            </p>
          )}
        </div>
      </aside>
    </div>
  );
}
