import ConvertContainer from "./components/ConvertContainer";
import "./App.css";

function App() {
  return (
    <>
      <header>Thanks for using Better Converter!</header>
      <main>
        <h1>Better Converter</h1>
        <ConvertContainer />
      </main>
      <footer>
        <div>
          Sorce code:
          <a href="https://github.com/Domza64/Better-Converter"> Github</a>
        </div>
        <div>
          My website:
          <a href="https://domza.xyz"> domza.xyz</a>
        </div>
        <div>
          Contact me:
          <a href="https://domza.xyz#contact"> HERE</a>
        </div>
      </footer>
    </>
  );
}

export default App;
