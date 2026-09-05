export default function ErrorMessage({ message = "Something went wrong." }) {
  return <p className="status-message error-message">{message}</p>;
}
