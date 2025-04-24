import { useEffect, useRef, useState } from "react";
import "./App.css";

function App() {
  const [ticker, setTicker] = useState(null);
  const [connected, setConnected] = useState(false);
  const socketRef = useRef(null);

  useEffect(() => {
    const socket = new WebSocket(
      "ws://localhost:8080/subscriptions",
      "graphql-transport-ws"
    );
    socketRef.current = socket;

    socket.onopen = () => {
      setConnected(true);
      socket.send(JSON.stringify({ type: "connection_init" }));
      socket.send(
        JSON.stringify({
          id: "1",
          type: "subscribe",
          payload: {
            query: `subscription { tickerChanged(symbol: \"BTC/USD\") { symbol price size } }`,
          },
        })
      );
    };

    socket.onmessage = (event) => {
      const msg = JSON.parse(event.data);
      if (msg.type === "next" && msg.payload?.data?.tickerChanged) {
        setTicker(msg.payload.data.tickerChanged);
      }
    };

    socket.onerror = (error) => {
      console.error("WebSocket error:", error);
    };

    socket.onclose = () => {
      setConnected(false);
    };

    return () => socket.close();
  }, []);

  return (
    <div className="p-4 max-w-md mx-auto text-center">
      <h1 className="text-2xl font-bold mb-4">Market Data Subscription</h1>
      <p className="mb-2">Status: {connected ? "Connected" : "Disconnected"}</p>
      {ticker ? (
        <div className="bg-green-100 p-4 rounded shadow">
          <p>
            <strong>Symbol:</strong> {ticker.symbol}
          </p>
          <p>
            <strong>Price:</strong> {ticker.price}
          </p>
          <p>
            <strong>Size:</strong> {ticker.size}
          </p>
        </div>
      ) : (
        <p className="text-gray-500">Waiting for updates...</p>
      )}
    </div>
  );
}

export default App;
