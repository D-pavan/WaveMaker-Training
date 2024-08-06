
var post_url="https://dummyjson.com/users/"; // api to which form details get posted
var fetch_url="https://randomuser.me/api";  // api to fetch random details

//function to fill form with fetched details from api
function fillForm()
{
  var response=fetch(fetch_url).then(response=>response.json()).then(data=>fillData(data)).catch(error => console.error('Error:', error));
}

// function to receive response as data and fills input fields
function fillData(data){

    document.getElementById('fname').value=data.results[0].name.first;
    document.getElementById('lname').value=data.results[0].name.last;
    document.getElementById('email').value=data.results[0].email;
    document.getElementById('password').value=data.results[0].login.password;
    document.getElementById('cpswd').value=data.results[0].login.password;
    document.getElementById('fileInput').style.display='none';
    document.getElementById('image').src=data.results[0].picture.medium;
    
    //gender
    if(data.results[0].gender=="male"){
      document.getElementById('male').checked=true;
    }
    else{
        document.getElementById('female').checked=true;
    }
    //age
    document.getElementById('age').value=data.results[0].dob.age;

    //Bio
    document.getElementById('bio').value="Hello,My name is "+data.results[0].name.first+" "+data.results[0].name.last+" From "+data.results[0].location.city+","+data.results[0].location.state+","+data.results[0].location.postcode;
    
    document.getElementById('inc-value').innerText=document.getElementById('income').value+"k";
}

//function to post form input's to api
function sendDetails(){

    const accountform=document.getElementById('AccountForm');
    const formData=new FormData(accountform);
    const data=Object.fromEntries(formData.entries());  //getting all form entries
    const hobbies=formData.getAll('hobbies');
    if(hobbies.length>0){
        data.hobbies=hobbies;
    }   
    else{
        delete data.hobbies;
    }
    
    //post details to api
    fetch(post_url,{
        method:'POST',
        headers : {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response=>{
        if(response.ok){
            alert("successfully posted !!");
        }
        else{
            alert(' There is an issue with network');
        }
    }).then(data=>alert(data)).catch(err=>alert(err.message));
   
    //clear form after post is done
     document.getElementById('fileInput').style.display='block';
     document.getElementById('image').style.display='none';
     accountform.reset()

}

// function to change salary
function changeSalary(){
    document.getElementById('inc-value').innerText=document.getElementById('income').value+"k";

}

//event listener to fill form when loaded
document.addEventListener('DOMContentLoaded',fillForm);
