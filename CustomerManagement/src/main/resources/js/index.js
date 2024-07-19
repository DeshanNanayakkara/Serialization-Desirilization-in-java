document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('form');

    form.addEventListener('submit', (event) => {
        event.preventDefault();

        const name = document.querySelector('#name').value;
        const email = document.querySelector('#email').value;
        const address = document.querySelector('#address').value;

        // console.log('Name:', name);
        // console.log('Email:', email);
        // console.log('Address:', address);

        //create an object for accumilate data
        const studentData = {
            name : name,
            email : email,
            address : address
        }

        // console.log(studentData);

        //create JSON
        const studentJSON = JSON.stringify(studentData);
        console.log(studentData);



        //Introduce AJAX
        const http = new XMLHttpRequest();

        http.onreadystatechange = () => {
            if (http.readyState == 4){
                if (http.status==200){
                    var responseTextJSON = JSON.stringify(http.responseText);
                    console.log(responseTextJSON);
                }else{
                    console.error("Failed");
                    console.error("Status"+http.status);
                    console.error("Ready Status"+http.readyState);
                }
            }else{
                console.error("Ready Status"+http.readyState);
            }
        }

        http.open("POST", "http://localhost:8080/StudentManagement/student", true)
        http.setRequestHeader("Content-Type", "application/json");
        http.send(studentData);

    });
  });