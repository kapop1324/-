import firebase from 'firebase';

var firebaseConfig = {
  apiKey: "AIzaSyByJs9HYDo22JOmuffA7s6aEYsILgHwDyc",
  authDomain: "websns-5f1d6.firebaseapp.com",
  projectId: "websns-5f1d6",
  storageBucket: "websns-5f1d6.appspot.com",
  messagingSenderId: "351362417253",
  appId: "1:351362417253:web:6854606b89e8f946767c8c"
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);

const db = firebase.firestore();
const storage = firebase.storage()

const noticeAdd = (data) => {
  const now = Date.now()
  db.collection("notice"+data.user).doc(now.toString()).set({
    date:now,
    img:data.img,
    ReqUser: data.ReqUser,
    type:data.type,
    articleID:data.articleID
  })
  .then((docRef) => {
  })
}

const upLoad = (file,callback) => {
  var storageRef = storage.ref()
  const now = Date.now()
  var path = storageRef.child('image/'+`${now.toString()}/`+file.name)
  var upload = path.put(file)

  upload.on('state_changed',
  null,
  (e)=>{
  },
  ()=>{
    upload.snapshot.ref.getDownloadURL().then((url)=>{
      callback(url)
    })
  })
}
const upLoadProfile = (file,callback) => {
  var storageRef = storage.ref()
  var path = storageRef.child('profile/'+`${file.userId}/`+file.img.name)
  var upload = path.put(file.img)

  upload.on('state_changed',
  null,
  (e)=>{
  },
  ()=>{
    upload.snapshot.ref.getDownloadURL().then((url)=>{
      callback(url)
    })
  })
}

const FirebaseApi = {
  noticeAdd:(data)=>noticeAdd(data),
  upLoad:(file,callback)=>upLoad(file,callback),
  upLoadProfile:(file,callback)=>upLoadProfile(file,callback),

}
export default FirebaseApi