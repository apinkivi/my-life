import {
  signInWithGoogle,
  signOutUser,
  syncTasks,
  onAuthStateChanged,
} from "./auth";
import "./style.css";

const app = document.querySelector<HTMLDivElement>("#app")!;

app.innerHTML = `
  <div class="container">
    <header>
      <h1>My Life</h1>
      <p class="subtitle">Task Management</p>
    </header>

    <main>
      <section id="auth-section" class="card">
        <div id="signed-out">
          <p>Kirjaudu sisään Google-tililläsi synkronoidaksesi tehtäväsi.</p>
          <button id="sign-in-btn" class="btn btn-primary">
            <svg width="18" height="18" viewBox="0 0 18 18" fill="none">
              <path d="M17.64 9.2c0-.637-.057-1.251-.164-1.84H9v3.481h4.844a4.14 4.14 0 0 1-1.796 2.716v2.259h2.908c1.702-1.567 2.684-3.875 2.684-6.615Z" fill="#4285F4"/>
              <path d="M9 18c2.43 0 4.467-.806 5.956-2.18l-2.908-2.259c-.806.54-1.837.86-3.048.86-2.344 0-4.328-1.584-5.036-3.711H.957v2.332A8.997 8.997 0 0 0 9 18Z" fill="#34A853"/>
              <path d="M3.964 10.71A5.41 5.41 0 0 1 3.682 9c0-.593.102-1.17.282-1.71V4.958H.957A8.997 8.997 0 0 0 0 9c0 1.452.348 2.827.957 4.042l3.007-2.332Z" fill="#FBBC05"/>
              <path d="M9 3.58c1.321 0 2.508.454 3.44 1.345l2.582-2.58C13.463.891 11.426 0 9 0A8.997 8.997 0 0 0 .957 4.958L3.964 6.29C4.672 4.163 6.656 2.58 9 3.58Z" fill="#EA4335"/>
            </svg>
            Kirjaudu Google-tilillä
          </button>
        </div>

        <div id="signed-in" class="hidden">
          <div class="user-info">
            <img id="user-avatar" class="avatar" alt="Avatar" />
            <div>
              <p id="user-name" class="user-name"></p>
              <p id="user-email" class="user-email"></p>
            </div>
          </div>
          <div class="actions">
            <button id="sync-btn" class="btn btn-primary">
              Synkronoi tehtävät
            </button>
            <button id="sign-out-btn" class="btn btn-secondary">
              Kirjaudu ulos
            </button>
          </div>
        </div>
      </section>

      <section id="status-section" class="card hidden">
        <div id="status-message"></div>
      </section>
    </main>

    <footer>
      <p>Apinkivi &middot; My Life</p>
    </footer>
  </div>
`;

// DOM elements.
const signedOutEl = document.getElementById("signed-out")!;
const signedInEl = document.getElementById("signed-in")!;
const signInBtn = document.getElementById("sign-in-btn")!;
const signOutBtn = document.getElementById("sign-out-btn")!;
const syncBtn = document.getElementById("sync-btn")!;
const userAvatar = document.getElementById("user-avatar") as HTMLImageElement;
const userName = document.getElementById("user-name")!;
const userEmail = document.getElementById("user-email")!;
const statusSection = document.getElementById("status-section")!;
const statusMessage = document.getElementById("status-message")!;

// Auth state listener.
onAuthStateChanged((user) => {
  if (user) {
    signedOutEl.classList.add("hidden");
    signedInEl.classList.remove("hidden");
    userAvatar.src = user.photoURL ?? "";
    userName.textContent = user.displayName ?? "User";
    userEmail.textContent = user.email ?? "";
  } else {
    signedOutEl.classList.remove("hidden");
    signedInEl.classList.add("hidden");
    statusSection.classList.add("hidden");
  }
});

// Sign in.
signInBtn.addEventListener("click", async () => {
  try {
    await signInWithGoogle();
  } catch (error) {
    showStatus("error", `Kirjautuminen epäonnistui: ${error}`);
  }
});

// Sign out.
signOutBtn.addEventListener("click", async () => {
  await signOutUser();
});

// Sync tasks.
syncBtn.addEventListener("click", async () => {
  syncBtn.setAttribute("disabled", "true");
  syncBtn.textContent = "Synkronoidaan...";
  showStatus("loading", "Haetaan tehtäviä Google Tasksista...");

  try {
    const result = await syncTasks();
    showStatus(
      "success",
      `Synkronointi valmis! ${result.taskLists} tehtävälistaa ja ${result.tasks} tehtävää tallennettu Firestoreen.`
    );
  } catch (error) {
    showStatus("error", `Synkronointi epäonnistui: ${error}`);
  } finally {
    syncBtn.removeAttribute("disabled");
    syncBtn.textContent = "Synkronoi tehtävät";
  }
});

function showStatus(type: "loading" | "success" | "error", message: string) {
  statusSection.classList.remove("hidden");
  statusMessage.className = `status status-${type}`;
  statusMessage.textContent = message;
}
