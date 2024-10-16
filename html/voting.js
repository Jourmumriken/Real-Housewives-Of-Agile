 function vote(type) {
    const username = localStorage.getItem("username");
    const guideID = parseInt(window.location.search.replace("[^0-9]",""));
    const voteData = {
            name: ""+username,
            type:  ""+type,
    }

    fetch('/vote',{
    method: "POST",
    headers: {
                'Content-Type': 'application/JSON',    // Data format
    },
    body: JSON.stringify(voteData),
    })
    .then(response => response)
    .then(data => console.log(response))
    .catch(error => {
            alert("HEY!")
        });
 }