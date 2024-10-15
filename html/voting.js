 function vote(type) {
    const username = localStorage.getItem("username");
    const voteData = {
            name: username,
            type: type
    }
    console.log();
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