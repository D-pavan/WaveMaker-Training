document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const username = document.getElementById('name').value;
    const password = document.getElementById('pwd').value;

    const data = {
        userName: username,
        password: password
    };

    fetch('http://localhost:8080/TODOAPP/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response =>{

        if(response.ok){
            window.location.href = 'http://localhost:8080/TODOAPP/index.html'
        }
        return response;
        
    })
    .catch(error => {
        console.error('Error:', error);
        alert('An error occurred while processing your request');
    });
});
