document.addEventListener('DOMContentLoaded', function() {
    const toggleButton = document.querySelector('.toggle'); // Select the toggle button
    const sidebar = document.querySelector('.side-nav'); // Select the sidebar
    const mainContainer = document.querySelector('.main-content-container'); // Select the main content container
  
    // Add event listener to the toggle button
    toggleButton.addEventListener('click', function () {
        // Toggle the hidden class on the sidebar
        sidebar.classList.toggle('side-nav--hidden');

        // Toggle the sidebar-hidden class on the main content container
    });
});


  
// alert("JavaScript loaded successfully!");
// Function to handle login functionality
function login() {
    //event.preventDefault(); //Uncomment this to prevent redirections after login, if you intend to get a response on the login page.
    // Get username and password input values
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const messageElement = document.getElementById('message'); // Element to display login messages

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

// Checks URL for error messages and displays them depending on error type that occured
function loginfailure(){
    const urlParameters = new URLSearchParams(window.location.search);
    const error = urlParameters.get('error');

    // Set error text depending on kind of error
    if(error === 'invalid') {
        document.getElementById('error-message').textContent = 'Invalid password.';
    } else if (error === 'notfound') {
        document.getElementById('error-message').textContent = 'Account does not exist.';
    }
}

// Create event listener to check for loginfailures
document.addEventListener('DOMContentLoaded', function(){ loginfailure(); });

// Function to handle registration functionality
function register() {
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

// Handle voting
function vote(input){
       const guideID = parseInt(window.location.search.replace(/[^0-9]/g,"")); // regex, remove all non digit characters
       console.log(guideID)
       if (input == 1){
            console.log("up")
            sendVoteToServer(guideID, 'up')
       }
       else{
                       console.log("down")
                       sendVoteToServer(guideID, 'down')
       }
//           .then(() => {
//               console.log("up")
//
//           })
//           .catch(error => console.error('Error upvoting:', error.text));}



//                   .then(() => {
//                       console.log("down")
//                   })
//                   .catch(error => console.error('Error downVoting:', error));}
   }

   function sendVoteToServer(guideId, voteType) {
       return fetch('/vote', {
           method: 'POST',
           headers: {
               'Content-Type': 'application/json'
           },
           body: JSON.stringify({
               voteType: voteType,
               guideId: guideId
           })
       })
       .then(response => {
           if (!response.ok) {
               throw new Error('Vote request failed');
           }
           location.reload();

       }).catch(error=> {
            console.error('Error:', error.message);
            alert(error);
       });
   }





//}

// ---------------------- //
// ----- iFixIt API ----- //
// ---------------------- //

// Fetches input value in search bar on external-guide.html
// and calls fetchGuide on search value.
// Fetches input value in search bar on external-guide.html
// and calls fetchGuide on search value.
document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.getElementById('searchInput');

    // Add event listener for keypress (specifically for the 'Enter' key)
    searchInput.addEventListener('keypress', function (event) {
        // Check if the pressed key is 'Enter'
        if (event.key === 'Enter') {
            event.preventDefault();  // Prevent form submission and page reload

            // Get the value from the input field
            const query = searchInput.value;

            // Call the fetchSuggestion function with the entered search query
            fetchSuggestions(query);

            // Reset search bar value
            searchInput.value = '';
        }
    });

    // Add event listener for dynamic fetching while typing
    searchInput.addEventListener('input', function () {
        const searchQuery = this.value;
        // If the query is > 2 characters, fetch results
        if (searchQuery.length > 2) {
            // Query the search results
            fetchSuggestions(searchQuery);
        } else {
            // Clear the suggestion list
            document.getElementById("suggestions").innerHTML = '';
        }
    });

});

// ------------------------------------------ //
// ----- Querying Suggestions by String ----- //
// ------------------------------------------ //

function fetchSuggestions(searchQuery) {
    // API URL to fetch guide from
    const apiUrl = `https://www.ifixit.com/api/2.0/suggest/${searchQuery}?doctypes=guide`;

    // Fetch the guide on the URL
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => displaySuggestions(data.results))
        .catch(error => console.error('Error fetching data:', error));
}

// Function to display the guide suggestions as clickable links
function displaySuggestions(suggestions) {
    const suggestionsElement = document.getElementById('suggestions');
    suggestionsElement.innerHTML = ''; // Clear any existing suggestions

    suggestions.forEach(suggestion => {
        const guideLink = document.createElement('a');
        // This is the file (extguide.html) where external guides will be displayed,
        // alongside the guideid that should be queried via fetchGuide(guideId) to obtain
        // the guide's content.
        guideLink.href = `extguide?guideID=${suggestion.guideid}`;
        guideLink.textContent = suggestion.title;
        guideLink.style.display = 'block'; // Each link should be on a new line

        suggestionsElement.appendChild(guideLink);
    });
}

// ------------------------------- //
// ----- Querying by GuideID ----- //
// ------------------------------- //

// Getting the guideID from the domain
// ex: (http://localhost:8080/extguide?guideID=140588) -> returns 140588
function getGuideIdFromURL() {
    const urlParameter = new URLSearchParams(window.location.search);
    return urlParameter.get('guideID');
}

// Add event listener which actively fetches guideIDs from url query
// And passes it onto an api call to fetch the guide, and
// furthermore create a channel for html to display it.
document.addEventListener('DOMContentLoaded', function () {
    const guideID = getGuideIdFromURL();
    if (guideID) {
        fetchGuide(guideID);
    } else {
        console.error("No guideID for external guide provided in URL.");
    }
});

// Querying by GuideID
function fetchGuide(guideId) {
    const apiUrl = `https://www.ifixit.com/api/2.0/guides/${guideId}`;

    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => displayData(data))
        .catch(error => console.error('Error fetching data:', error));
}

// Display fetched data into the page
function displayData(data) {
    // Set guide title
    document.getElementById('guide-title').textContent = data.title;

    // Set introduction
    const introductionElement = document.getElementById('introduction');
    introductionElement.innerHTML = `<h3> Introduction </h3><br>` + data.introduction_rendered;

    // Populate steps
    const stepsElement = document.getElementById('steps');
    stepsElement.innerHTML = ''; // Clear any existing steps
    let stepCounter = 1; // Counter for the step number
    data.steps.forEach(step => {
        const stepElement = document.createElement('div');
        stepElement.setAttribute("id", `step-${stepCounter}`);
        stepElement.innerHTML = `<br><h3>Step ${stepCounter}: ${step.title || ''}</h3>`;
        step.lines.forEach(line => {
            const lineElement = document.createElement('p');
            lineElement.innerHTML = line.text_rendered;
            stepElement.appendChild(lineElement);
        });
        stepsElement.appendChild(stepElement);

        if (step.media && step.media.type == "image" && step.media.data && step.media.data.length > 0) {
            let imageCounter = 0;
            step.media.data.forEach(image => {
                const imageData = step.media.data[imageCounter];
                const imgElement = document.createElement('img');
                imgElement.src = imageData["standard"];
                stepElement.appendChild(imgElement);
                imageCounter++;
            })
        }

        // Increase step counter
        stepCounter++;
    });

    // Populate table of contents
    const tocElement = document.getElementById('table-of-contents');
    tocElement.innerHTML = ''; // Clear existing contents
    let tocStepCounter = 1; // Counter for the step number in the table of contents
    data.steps.forEach(step => {
        const tocItem = document.createElement('li');
        const stepTitleIfExists = step.title ? `: ${step.title}` : '';
        tocItem.innerHTML = `<a href="#step-${tocStepCounter}">Step ${tocStepCounter}${stepTitleIfExists}</a>`;
        tocElement.appendChild(tocItem);
        tocStepCounter++;
    });
}



