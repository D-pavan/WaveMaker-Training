var subtaskId=-1;
function add(){
    document.getElementById('task-form').style.display='block';
}

async function done(event){
    event.preventDefault();
    const task={
        "title":document.getElementById('title').value,
        'description':document.getElementById('description').value,
        'duration':document.getElementById('date').value+' '+document.getElementById('duration').value+':00',
        'priority':document.getElementById('priority').value
    };
    if(validateDate(parseDate(task['duration']))){
        await postTask(task);
        document.getElementById('task-form').style.display='none';
        checkTaskDurations();
    }
    else alert("Date or time can't be past");
}

async function postTask(task) {
    const url = 'http://localhost:8080/TODOAPP/tasks';

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(task)
        });
        if(response.status==409){
            const data=await response.json();
            alert(data.message);
        }
        else if (!response.ok) {
            alert("unexpected error occured")
            throw new Error(`Network Error: ${response.status} ${response.statusText}`);
        }
        else{
        const data = await response.json();
       
        createTaskCard(data);
        }
    } catch (error) {
        console.error("Error posting the task:", error);
        
    }
}
function createTaskCard(task){

            const mainDiv = document.createElement('div');
            mainDiv.className = 'col-md-12 mb-1 bg-light border rounded border-dark';
            mainDiv.id='div_id'+task["id"];
            mainDiv.draggable = true;

            const rowDiv = document.createElement('div');
            rowDiv.className = 'row draggable';
           
            const leftCol = document.createElement('div');
            leftCol.className = 'col-md-6';
            leftCol.id = 'col'+task["id"]; 

            const innerRow = document.createElement('div');
            innerRow.className = 'row d-flex';

            const checkboxCol = document.createElement('div');
            checkboxCol.className = 'col-md-1 mt-4 mx-4';
            const checkbox = document.createElement('input');
            checkbox.id='chkb'+task["id"];
            checkbox.type = 'checkbox';
            checkbox.onchange=function(event){
                 strikeTask(checkbox.id);
            }
            checkboxCol.appendChild(checkbox);

            const desc=document.createElement('p');
            desc.id='desc'+task["id"];
            desc.textContent="Description : "+task["description"];

            const time=document.createElement('p');
            time.id='time'+task["id"];
            time.textContent="Duration : "+task["duration"];

            const  priority=document.createElement('p');
            priority.id='prio'+task["id"];
            priority.textContent="Priority : "+task["priority"];

            desc.style.display='none';
            time.style.display='none';
            priority.style.display='none';

            const textCol = document.createElement('div');
            textCol.className = 'col-md-6';

            const heading = document.createElement('h3');
            heading.id='title_id'+task["id"];
            heading.textContent=task["title"];
            heading.className = 'm-4';

            textCol.appendChild(heading);
            innerRow.appendChild(checkboxCol);
            innerRow.appendChild(textCol);           
            leftCol.appendChild(innerRow);

            const rightCol = document.createElement('div');
            rightCol.className = 'col-md-6 d-md-flex flex-md-row d-sm-flex flex-sm-column justify-content-end';           
            
            const buttonEdit = document.createElement('button');
            buttonEdit.style.height='50px';
            buttonEdit.className = 'btn btn-dark  mx-1 my-4';
            buttonEdit.id='edit'+task["id"];
            buttonEdit.textContent = 'Edit';
            buttonEdit.onclick=function(event){
                openDialog(event,buttonEdit.id);
            }

            const buttonView = document.createElement('button');
            buttonView.style.height='50px';
            buttonView.className = 'btn btn-dark  mx-1 my-4';
            buttonView.id='view'+task["id"];
            buttonView.textContent = 'View';
            buttonView.onclick = function(event){
                viewTask(event,buttonView.id);
            };

            const buttonDelete = document.createElement('button');
            buttonDelete.style.height='50px';
            buttonDelete.className = 'btn btn-dark text-center  mx-1 my-4';
            buttonDelete.id='bttn'+task["id"];
            buttonDelete.textContent = 'Delete';
            buttonDelete.onclick=function(event){
                deleteTask(event,buttonDelete.id);
            };

            const subTaskbtn = document.createElement('button');
            subTaskbtn.style.height='50px';
            subTaskbtn.className = 'btn btn-dark mx-1 my-4  text-center';
            subTaskbtn.id='subt'+task["id"];
            subTaskbtn.textContent = 'Subtask';
            subTaskbtn.onclick=function(event){
                addsubTask(event,subTaskbtn.id);
            };

            leftCol.appendChild(desc);
            leftCol.appendChild(time);
            leftCol.appendChild(priority);

            rightCol.appendChild(buttonEdit);
            rightCol.appendChild(buttonView);
            rightCol.appendChild(buttonDelete);
            rightCol.appendChild(subTaskbtn);

            rowDiv.appendChild(leftCol);
            rowDiv.appendChild(rightCol);   

            mainDiv.appendChild(rowDiv);
            document.getElementById('task-container').appendChild(mainDiv);
            
            if(subtaskId>0){
                mainDiv.setAttribute("parentId","div_id"+subtaskId)
                document.getElementById('div_id'+subtaskId).appendChild(mainDiv);
            }   
            else{
                document.getElementById('task-container').appendChild(mainDiv);
            }
}

//theme change
var theme=1;
function changeTheme() {

    if(theme==1){
        document.getElementById('app-container').style.backgroundColor='black';
        document.getElementById('body_bg').style.backgroundColor='black'
        document.getElementById('heading1').className='mx-4 text-white';
        document.getElementById('t_title').className='mx-4 text-white';

        theme=0;
    }
    else{
        document.getElementById('app-container').style.backgroundColor='rgb(250, 250, 247)';
        document.getElementById('body_bg').style.backgroundColor='white';
        document.getElementById('heading1').className='mx-4 text-dark';
        document.getElementById('t_title').className='mx-4 text-dark';

        theme=1;
    }
}

//view task functionality
function viewTask(event,btnid){
    event.preventDefault();
    let id=getId(btnid);
    try{
        let isViewing = document.getElementById('desc'+id).style.display
        if(isViewing==='none'){
            document.getElementById('desc'+id).style.display='block';
            document.getElementById('prio'+id).style.display='block';
            document.getElementById('time'+id).style.display='block';
        }
        else{
            document.getElementById('desc'+id).style.display='none';
            document.getElementById('prio'+id).style.display='none';
            document.getElementById('time'+id).style.display='none';
        }
    }
    catch(e){
        console.log("error occurred while viewing the task",e)
        alert("An Error occured while viewing the task")
    }
    checkTaskDurations()
}
//edit task
let edit_id=-1;
function openDialog(event,editBtnId) {
    if(editBtnId==-1){
        document.getElementById('dialog').classList.add('show');
        document.getElementById('overlay').classList.add('show');
    }
    else{
        document.getElementById('dialog').classList.add('show');
        document.getElementById('overlay').classList.add('show');
        edit_id=editBtnId; 
    }
    
}

function closeDialog() {
    document.getElementById('dialog').classList.remove('show');
    document.getElementById('overlay').classList.remove('show');
}

async function submitForm() {
    if(subtaskId<0){
        const title = document.getElementById('d_title').value;
        const description = document.getElementById('d_description').value;
        const time=document.getElementById('d_duration').value;
        const date=document.getElementById('d_date').value;
        const priority = document.getElementById('d_priority').value;

        const id=getId(edit_id);

        document.getElementById('title_id'+id).innerText=title;
        document.getElementById('desc'+id).innerText='description : '+description;
        document.getElementById('time'+id).innerText='duration: '+time+' date: '+date;
        document.getElementById('prio'+id).innerText='priority :'+priority;
        let task={'title':title,'description':description,'duration':date+' '+time+':00','priority':priority};
        
        await updateTask(task,id);
    }
    else{
        const title = document.getElementById('d_title').value;
        const description = document.getElementById('d_description').value;
        const time=document.getElementById('d_duration').value;
        const date=document.getElementById('d_date').value;
        const priority = document.getElementById('d_priority').value;
   
        document.getElementById('title_id'+id).innerText=title;
        let task={'id':id,'details':{'title':title,'description':description,'duration':time,'date':date,'priority':priority}};
        localStorage.setItem('task'+id,JSON.stringify(task));
        subt_parent['task'+id]='main'+subtaskId;
        tasks.push('task'+id);
        id++;
        console.log(tasks);
        saveData();
    }
    checkTaskDurations();
    closeDialog(); 
    
}
async function updateTask(task, id) {
    const url = `http://localhost:8080/TODOAPP/tasks?id=${id}`;

    try {
   
        const response = await fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(task)
        });

        if (!response.ok) {
            throw new Error(`Network error: ${response.status} ${response.statusText}`);
        }

        const data = await response.json();
        console.log("Update successful:", data);
    } catch (error) {
        console.error("Error updating the task:", error);
     
    }
}


//delete Task
async function deleteTask(event, div_id) {
    const taskElementId = 'div_id' + getId(div_id);
    const taskElement = document.getElementById(taskElementId);
    if (taskElement) {
        taskElement.remove();
    } else {
        console.warn(`Task element with ID ${taskElementId} not found.`);
    }

    const id1 = getId(div_id);
    const url = `http://localhost:8080/TODOAPP/tasks?id=${id1}`;

    try {
    
        const response = await fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            throw new Error(`Network error: ${response.status} ${response.statusText}`);
        }
        const data = await response.json();
        console.log("Task deleted successfully:", data);
        
    } catch (error) {
        console.error("Error deleting the task:", error);
    }
}

async function loadData() {
    const url = 'http://localhost:8080/TODOAPP/tasks';
    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if(response.status==401){
            window.location.href = 'http://localhost:8080/TODOAPP/login.html'
        }
        else if (response.ok) {
                    if(response.headers.get('logged')==='false')   window.location.href = 'http://localhost:8080/TODOAPP/login.html'

                    else{
                        const data = await response.json();
                        console.log("Fetch successful");

                        const values = Object.values(data);
                        values.forEach(element => {
                            createTaskCard(element);
                        });
                    }

        }
        else{
             throw new Error(`Network error: ${response.status} ${response.statusText}`);
        }

    } catch (error) {
        console.error("Error loading data:", error);
    }
}

//strikeTask on deadline or checkbox click
function strikeTask(id){

    if(document.getElementById('chkb'+getId(id)).checked==true || id.startsWith("v")){
        document.getElementById('title_id'+getId(id)).style.textDecoration='line-through';
    }
    else{
        document.getElementById('title_id'+getId(id)).style.textDecoration='none';
    }
}

function getId(name){
    return parseInt(name.substring(4,name.length));
}

//sorting the tasks
function sortCards(){
            
        const container = document.getElementById('task-container');
        const tasks = Array.from(container.children);
        const sortOption = document.getElementById('sortoption').value;

        if(sortOption==='priority'){
            const priorityOrder = { 'low': 1, 'medium': 2, 'high': 3 };
            tasks.sort((a, b) => {
                const priorityA = a.querySelector('p[id^="prio"]').textContent.split(': ')[1].toLowerCase();
                const priorityB = b.querySelector('p[id^="prio"]').textContent.split(': ')[1].toLowerCase();
                return priorityOrder[priorityB] - priorityOrder[priorityA];
            });
        }
        else{
            tasks.sort((a, b) => {
                const timeA = a.querySelector('p[id^="time"]').textContent.split(': ')[1];
                const timeB = b.querySelector('p[id^="time"]').textContent.split(': ')[1];
                return parseDate(timeA) - parseDate(timeB);
            });
        }
        
        container.innerHTML = '';
        tasks.forEach(task => container.appendChild(task));
}

//formatting the date
function parseDate(dateString){
    return new Date(dateString.replace(' ', 'T'));
}

function search(){
    const searchInput = document.getElementById('search').value.toLowerCase();
    const tasks = Array.from(document.getElementById('task-container').children);
    tasks.forEach(task => {
            const title = task.querySelector('h3').textContent.toLowerCase();
            if (title.includes(searchInput)) {
                    task.style.display = '';
            } else {
                    task.style.display = 'none';
            }
    });
}

function checkTaskDurations(){
    const tasks = Array.from(document.getElementById('task-container').children);

    tasks.forEach(task=>{
        const taskTime=parseDate(task.querySelector('p[id^="time"]').textContent.split(': ')[1]);
        const now = new Date();
        const timeDifference = (taskTime - now) / 1000 / 60 / 60; // Convert milliseconds to hours

        if (timeDifference < 1) {
            let title = task.querySelector('h3').textContent;
            let description=task.querySelector('p[id^="desc"]').textContent.split(": ")[1];
            if(timeDifference>0)
                 sendNotification(title, description,Math.floor(timeDifference*60)+"mins");
            else {
                strikeTask(task.id.substring(2));
                let time=Math.abs(Math.floor(timeDifference));
                sendNotification(title, description,"Task time completed "+time+"mins ago");
            }
        }
    });
}
function sendNotification(title, description, duration) {
    if (Notification.permission === 'granted') {
        new Notification('TODO App', {
            body: `Task: ${title}\nDescription: ${description}\nDuration: ${duration}`,
            icon: 'https://image.shutterstock.com/image-vector/clipboard-pencil-icon-vector-illustration-260nw-270889529.jpg' 
        });
    } else if (Notification.permission !== 'denied') {
        Notification.requestPermission().then(permission => {
            if (permission === 'granted') {
                sendNotification(title, description, duration);
            }
        });
    }
}
document.addEventListener('DOMContentLoaded', () => {
    const taskContainer = document.getElementById('task-container');

    taskContainer.addEventListener('dragstart', (e) => {
        e.dataTransfer.setData('text/plain', e.target.id); 
        e.target.classList.add('dragging'); 
    });

    taskContainer.addEventListener('dragover', (e) => {
        e.preventDefault(); 
    });

    taskContainer.addEventListener('drop', (e) => {
        e.preventDefault();
        const draggingElement = document.querySelector('.dragging');
        taskContainer.appendChild(draggingElement); 
        draggingElement.classList.remove('dragging'); 
    });

    taskContainer.addEventListener('dragend', (e) => {
        e.target.classList.remove('dragging'); 
    });
});

//add subtask
function addsubTask(event,subTaskbtnId){
    subtaskId = getId(subTaskbtnId);
    add();
}
function validateDate(date){
      const now=new Date();
      const timeDifference=date-now;
      return timeDifference>0;
}

function logOut() {
    fetch('http://localhost:8080/TODOAPP/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (response.ok) {

            window.location.href = 'http://localhost:8080/TODOAPP/login.html';
        } else {
            console.log('Logout failed:', response.statusText);
        }
    }).catch(error => {
        console.log('Error:', error);
    });
}

document.addEventListener('DOMContentLoaded',changeTheme);
document.addEventListener('DOMContentLoaded',async function(){
    document.getElementById("task-form").style.display='none';
    document.getElementById('sortoption').value='priority';
    const x=await loadData();
    checkTaskDurations();
});
