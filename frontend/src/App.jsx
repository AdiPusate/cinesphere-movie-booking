import { Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import ProtectedRoute from './components/ProtectedRoute';
import Home from './pages/Home';
import MovieDetails from './pages/MovieDetails';
import SeatSelection from './pages/SeatSelection';
import { Login, Register, ForgotPassword } from './pages/AuthPages';
import MyBookings from './pages/MyBookings';
import BookingConfirmation from './pages/BookingConfirmation';
import Admin from './pages/Admin';
import Profile from './pages/Profile';

export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        {/* Public */}
        <Route path="/" element={<Home />} />
        <Route path="/movies/:id" element={<MovieDetails />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        
        {/* Protected */}
        <Route element={<ProtectedRoute />}>
          <Route path="/shows/:showId/seats" element={<SeatSelection />} />
          <Route path="/booking/:bookingId" element={<BookingConfirmation />} />
          <Route path="/my-bookings" element={<MyBookings />} />
          <Route path="/profile" element={<Profile />} />
        </Route>

        {/* Admin */}
        <Route element={<ProtectedRoute requireAdmin />}>
          <Route path="/admin" element={<Admin />} />
        </Route>
      </Route>
    </Routes>
  );
}
