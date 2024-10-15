document.addEventListener('DOMContentLoaded', function() {
    const toggleButton = document.querySelector('.toggle'); // Select the toggle button
    const sidebar = document.querySelector('.side-nav'); // Select the sidebar
    const mainContainer = document.querySelector('.main-content-container'); // Select the main content container
    // Add event listener to the toggle button
//    toggleButton.addEventListener('click', function() {
//      // Toggle the hidden class on the sidebar
//      sidebar.classList.toggle('side-nav--hidden');
//
////      // Toggle the sidebar-hidden class on the main content container
//    });
  });
  

  
// alert("JavaScript loaded successfully!");
// Function to handle login functionality
function login() {
    //event.preventDefault(); //Uncomment this to prevent redirections after login, if you intend to get a response on the login page.
    // Get username and password input values

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const messageElement = document.getElementById('message'); // Element to display login messages

/*
    //check if the username and password are correct
    if (username === "test" && password === "test123") {
        messageElement.innerHTML = `<p style="color: green;">Login successful! Welcome, ${username}.</p>`;
        // we should load the admin page. 
        //const newUrl = `/guide2`;
        //window.history.pushState({ path: newUrl }, '', newUrl);

    } else {
        alert("Wrong!!!")
        messageElement.innerHTML = `<p style="color: red;">Invalid username or password.</p>`;
    }
        */ 

    // Send login credentials to the server using a POST request
    fetch('/auth', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',    // Data format
        },
        body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`   // URL encoded data
    })
    .then(response => response.text())  // Convert server response to text
    .then(data => {
        // Display server response in the message element
        document.getElementById('message').innerText = data;

        localStorage.setItem("username",username);
    })
    .catch(error => {
        document.getElementById('message').innerText = 'Error: ' + error;
    }); 

}

// Function to handle registration functionality
function register(){
    // Prevent the form from submitting and reloading the page
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const messageElement = document.getElementById('message');


        fetch('/registerNew', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`
    })
    .then(response => response.text())
    .then(data => {
        document.getElementById('message').innerText = data;
    })
    .catch(error => {
        document.getElementById('message').innerText = 'Error: ' + error;
    }); 
}


/* Toggle side nav bar*/
function toggleSideNav() {
    document.getElementById("toggle-sidebar").classList.toggle("collapsed");
    document.getElementById("toggle-main").classList.toggle("collapsed");
  }

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


