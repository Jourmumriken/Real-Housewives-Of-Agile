window.addEventListener("DOMContentLoaded", () => {
    const toggleButton = document.querySelector(".toggle"); 
    const sideNavEl = document.querySelector(".side-nav"); 

    toggleButton.addEventListener("click", () => {
        sideNavEl.classList.toggle("side-nav--hidden"); 
        
     
        
    });
    


});