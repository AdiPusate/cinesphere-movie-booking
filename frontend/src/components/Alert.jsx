export default function Alert({ children, type = 'error' }) {
  if (!children) return null;
  return (
    <div className={`alert ${type}`}>
      {children}
    </div>
  );
}
