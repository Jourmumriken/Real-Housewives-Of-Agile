
/* window.addEventListener("DOMContentLoaded", () => {
    const toggleButton = document.querySelector(".toggle"); 
    const sideNavEl = document.querySelector(".side-nav"); 
    const mainWithSide = document.querySelector(".")
    toggleButton.addEventListener("click", () => {
        sideNavEl.classList.toggle("side-nav--hidden"); 
        
     
        
    });
    


});
*/

document.addEventListener('DOMContentLoaded', function() {
    const toggleButton = document.querySelector('.toggle'); // Select the toggle button
    const sidebar = document.querySelector('.side-nav'); // Select the sidebar
    const mainContainer = document.querySelector('.main-content-container'); // Select the main content container
  
    // Add event listener to the toggle button
    toggleButton.addEventListener('click', function() {
      // Toggle the hidden class on the sidebar
      sidebar.classList.toggle('side-nav--hidden');
      
      // Toggle the sidebar-hidden class on the main content container
    });
  });
  

  
