// alert("JavaScript loaded successfully!");
// Function to handle login functionality
function login() {
    
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

// Sample JSON object representing a guide
const guides = [ // Changed from guide to guides
    {
      "id": 1,
      "title": "How to Fix a Broken Screen",
      "content": "This guide will help you repair a broken screen on your smartphone. Make sure to gather all necessary tools and follow the steps carefully.",
      "account": {
        "username": "user123",
        "email": "user123@example.com"
      },
      "difficulty": 3
    },
    {
      "id": 2,
      "title": "How to Replace a Battery",
      "content": "Learn how to replace the battery in your smartphone. This guide includes a list of tools and step-by-step instructions.",
      "account": {
        "username": "user456",
        "email": "user456@example.com"
      },
      "difficulty": 2
    },
    {
      "id": 3,
      "title": "Cleaning a Camera Lens",
      "content": "This guide covers the steps to properly clean your camera lens without damaging it. Recommended products and techniques are included.",
      "account": {
        "username": "user789",
        "email": "user789@example.com"
      },
      "difficulty": 1
    },
    {
      "id": 4,
      "title": "Repairing a Headphone Jack",
      "content": "Find out how to fix a malfunctioning headphone jack. This guide provides detailed instructions and necessary tools.",
      "account": {
        "username": "user101",
        "email": "user101@example.com"
      },
      "difficulty": 4
    },
    {
      "id": 5,
      "title": "Upgrading RAM in a Laptop",
      "content": "A complete guide on how to upgrade the RAM in your laptop for improved performance. Tools required and step-by-step instructions are included.",
      "account": {
        "username": "user202",
        "email": "user202@example.com"
      },
      "difficulty": 3
    }
  ];
// Function to dynamically update the list in index.html
function updateList() {
    const itemContainer = document.getElementById('item-container');
    itemContainer.innerHTML = '';

    guides.forEach(guide => { // Ensure you're using 'guides' here
      const listItem = document.createElement('li');
      const link = document.createElement('a');
      link.href = `guide${guide.id}.html`;
      link.textContent = guide.title;

      listItem.appendChild(link);
      itemContainer.appendChild(listItem);
    });
  }
  
  

  // Call the function when the page loads
  window.onload = updateList();