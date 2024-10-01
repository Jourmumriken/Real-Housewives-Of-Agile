// alert("JavaScript loaded successfully!");
// Function to handle login functionality
function login() {
    event.preventDefault();
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

