import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getFunctions } from "firebase/functions";

/**
 * Firebase configuration.
 *
 * TODO: Replace with your actual Firebase project config from
 * Firebase Console → Project Settings → General → Your apps.
 */
const firebaseConfig = {
  apiKey: "YOUR_API_KEY",
  authDomain: "YOUR_PROJECT.firebaseapp.com",
  projectId: "YOUR_PROJECT",
  storageBucket: "YOUR_PROJECT.firebasestorage.app",
  messagingSenderId: "YOUR_SENDER_ID",
  appId: "YOUR_APP_ID",
};

const app = initializeApp(firebaseConfig);

export const auth = getAuth(app);
export const functions = getFunctions(app, "europe-west1");
