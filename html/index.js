document.addEventListener('DOMContentLoaded', function () {
    createCenterList();
});

// appends guides to content the div
function appendGuides(guides,contentDiv) {
              guides.forEach(guide => {
                  const li = document.createElement('li');
                  const a = document.createElement('a');

                  a.textContent = guide.title;
                  a.href = guide.url;


        li.appendChild(a);
        contentDiv.append(li);
    });
}
function createCenterList() {
    console.log("createCenterList");
    fetch("/allGuides")
        .then(response => response.json())
        .then(data => {
            const contentDiv = document.getElementById('centered-list');
            console.log(data);
            appendGuides(data, contentDiv);

        })
        .catch(error => {
            console.error('Error fetching guides:', error);
        });

}
