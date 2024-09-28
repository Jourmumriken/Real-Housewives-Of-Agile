// alert("JavaScript loaded successfully!");

function login() {
    
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const messageElement = document.getElementById('message');

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
    fetch('/auth', {
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


function register(){
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

