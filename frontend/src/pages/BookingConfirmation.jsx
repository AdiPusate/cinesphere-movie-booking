import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, CheckCircle2, Ticket } from 'lucide-react';
import api, { errorMessage, unwrap } from '../api/api';
import Alert from '../components/Alert';

export default function BookingConfirmation() {
  const { bookingId } = useParams();
  const nav = useNavigate();
  const [booking, setBooking] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetch = async () => {
      try {
        const res = await api.get(`/bookings/${bookingId}`);
        setBooking(unwrap(res));
      } catch (err) {
        setError(errorMessage(err, 'Booking not found.'));
      } finally {
        setLoading(false);
      }
    };
    fetch();
  }, [bookingId]);

  if (loading) return <div className="page-loader"><span className="spinner"/></div>;

  if (error || !booking) {
    return (
      <div className="container" style={{ maxWidth: '600px' }}>
        <Alert>{error || 'Booking could not be loaded.'}</Alert>
        <button className="outline-button" onClick={() => nav('/')}><ArrowLeft size={15}/> RETURN HOME</button>
      </div>
    );
  }

  const handlePrint = async () => {
    try {
      const res = await api.get(`/receipt/${bookingId}`, { 
        responseType: 'blob',
        headers: { Accept: 'text/html' }
      });
      const htmlUrl = URL.createObjectURL(new Blob([res.data], { type: 'text/html' }));
      const printWindow = window.open(htmlUrl, '_blank');
      if (printWindow) {
        printWindow.onload = () => {
          printWindow.print();
        };
      }
    } catch (e) {
      alert('Could not retrieve receipt. Ensure you are authorized.');
    }
  };

  const handleDownloadPdf = async () => {
    try {
      const res = await api.get(`/receipt/${bookingId}/pdf`, { 
        responseType: 'blob',
        headers: { Accept: 'application/pdf' }
      });
      const pdfUrl = URL.createObjectURL(new Blob([res.data], { type: 'application/pdf' }));
      const a = document.createElement('a');
      a.href = pdfUrl;
      a.download = `receipt_${bookingId}.pdf`;
      a.click();
      URL.revokeObjectURL(pdfUrl);
    } catch (e) {
      alert('Could not retrieve PDF receipt. Ensure you are authorized.');
    }
  };

  return (
    <div className="container" style={{ maxWidth: '600px', textAlign: 'center', padding: '4rem 0' }}>
      <CheckCircle2 size={48} color="var(--success-color)" style={{ margin: '0 auto 1.5rem auto' }} />
      <h1 style={{ marginBottom: '0.5rem' }}>Transaction Complete</h1>
      <p style={{ color: 'var(--text-secondary)', marginBottom: '3rem' }}>Your access is secured.</p>

      <div style={{ border: '1px solid var(--border-color)', background: 'var(--surface-color)', padding: '2rem', textAlign: 'left', marginBottom: '3rem' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', borderBottom: '1px solid var(--border-color)', paddingBottom: '1.5rem', marginBottom: '1.5rem' }}>
          <div>
            <div className="eyebrow">BOOKING REF</div>
            <h2 style={{ margin: 0 }}>#{booking.bookingId.toString().padStart(6, '0')}</h2>
          </div>
          <div style={{ textAlign: 'right' }}>
            <div className="eyebrow">AMOUNT</div>
            <h2 style={{ margin: 0 }}>₹{Number(booking.totalAmount).toFixed(2)}</h2>
          </div>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2rem', marginBottom: '1.5rem' }}>
          <div>
            <div className="eyebrow" style={{ color: 'var(--text-primary)' }}>TITLE</div>
            <div style={{ fontWeight: '600' }}>{booking.show?.movie?.title}</div>
          </div>
          <div>
            <div className="eyebrow" style={{ color: 'var(--text-primary)' }}>LOCATION</div>
            <div style={{ fontWeight: '600' }}>{booking.show?.theatre?.name}</div>
          </div>
          <div>
            <div className="eyebrow" style={{ color: 'var(--text-primary)' }}>TIME</div>
            <div style={{ fontWeight: '600' }}>{new Date(booking.show?.showTime).toLocaleString()}</div>
          </div>
          <div>
            <div className="eyebrow" style={{ color: 'var(--text-primary)' }}>SEATS</div>
            <div style={{ fontWeight: '600' }}>{booking.bookedSeats?.join(', ')}</div>
          </div>
        </div>
      </div>

      <div style={{ display: 'flex', justifyContent: 'center', gap: '1rem', flexWrap: 'wrap' }}>
        <button className="primary-button" onClick={handlePrint}>
          PRINT PASS <Ticket size={15}/>
        </button>
        <button className="outline-button" onClick={handleDownloadPdf}>
          DOWNLOAD PDF
        </button>
        <button className="outline-button" onClick={() => nav('/')}>
          RETURN
        </button>
      </div>
    </div>
  );
}
