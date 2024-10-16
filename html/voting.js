// function vote(type) {
//    const username = localStorage.getItem("username");
//    const guideID = parseInt(window.location.search.replace("[^0-9]",""));
//    const voteData = {
//            name: ""+username,
//            type:  ""+type,
//    }
//
//    fetch('/vote',{
//    method: "POST",
//    headers: {
//                'Content-Type': 'application/JSON',    // Data format
//    },
//    body: JSON.stringify(voteData),
//    })
//    .then(response => response)
//    .then(data => console.log(response))
//    .catch(error => {
//            alert("HEY!")
//        });
// }
 function vote(input){

     if (input = 1){sendVoteToServer(guide.id, 'upvote')
         .then(() => {
             console.log("up")

         })
         .catch(error => console.error('Error upvoting:', error));}

         else{sendVoteToServer(guide.id, 'downvote')
                 .then(() => {
                     console.log("down")
                 })
                 .catch(error => console.error('Error downVoting:', error));}
 }

 function sendVoteToServer(guideId, voteType) {
     return fetch('/vote', {
         method: 'POST',
         headers: {
             'Content-Type': 'application/json'
         },
         body: JSON.stringify({
             guideId: guideId,
             voteType: voteType
         })
     })
     .then(response => {
         if (!response.ok) {
             throw new Error('Vote request failed');
         }
         return response.json();
     });
 }