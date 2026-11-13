import { useState } from "react";

// SURPRISE FEATURE: Trip Preparation Checklist.
// Only React state is used (no backend). Ticks are lost when the page is refreshed.
function TripChecklist() {
  const [items, setItems] = useState([
    { text: "Carry identity proof", done: false },
    { text: "Check hotel booking confirmation", done: false },
    { text: "Pack clothes", done: false },
    { text: "Carry mobile charger", done: false },
    { text: "Check travel tickets", done: false },
  ]);

  // Flip done/not done for the item at the given position
  function toggleItem(position) {
    const newItems = [];
    for (let i = 0; i < items.length; i++) {
      if (i === position) {
        newItems.push({ text: items[i].text, done: !items[i].done });
      } else {
        newItems.push(items[i]);
      }
    }
    setItems(newItems);
  }

  let doneCount = 0;
  for (let i = 0; i < items.length; i++) {
    if (items[i].done) doneCount++;
  }

  return (
    <div className="card checklist-card">
      <div className="card-body">
        <h5 className="card-title">🧳 Trip Preparation Checklist</h5>
        <p className="text-muted">{doneCount} of {items.length} items ready</p>
        <div className="progress mb-3" style={{ height: "8px" }}>
          <div className="progress-bar bg-success" style={{ width: (doneCount / items.length) * 100 + "%" }}></div>
        </div>
        {items.map((item, index) => (
          <div className="form-check mb-2" key={index}>
            <input
              className="form-check-input"
              type="checkbox"
              id={"check-" + index}
              checked={item.done}
              onChange={() => toggleItem(index)}
            />
            <label
              className={"form-check-label" + (item.done ? " text-decoration-line-through text-muted" : "")}
              htmlFor={"check-" + index}
            >
              {item.text}
            </label>
          </div>
        ))}
        {doneCount === items.length && <div className="alert alert-success mt-3 mb-0">You are all set. Happy travels! 🎉</div>}
      </div>
    </div>
  );
}

export default TripChecklist;
