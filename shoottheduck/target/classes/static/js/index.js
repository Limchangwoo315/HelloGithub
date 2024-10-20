import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";

// Firebase 설정 정보 (Firebase Console에서 복사)
const firebaseConfig = {
  apiKey: "AIzaSyDJgZtL7mCJDT7nPi93IuF7dn2soLFo77c",
  authDomain: "shoottheduck-8e6de.firebaseapp.com",
  projectId: "shoottheduck-8e6de",
  storageBucket: "shoottheduck-8e6de.appspot.com",
  messagingSenderId: "235818646053",
  appId: "1:235818646053:web:ada8f6a92fac8b508dddcc",
  measurementId: "G-DBL6DR4P2G"
};

// Firebase 앱 초기화
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);
