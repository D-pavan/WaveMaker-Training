var id=0;
var tasks=[];
var subt_parent={};
var subtaskId=-1;
function add(){
    document.getElementById('task-form').style.display='block';
}
function done(event){
      event.preventDefault();
      createTaskCard(-1,-1);
      const task={"id":id,"details":{"title":document.getElementById('title').value,'description':document.getElementById('description').value,'duration':document.getElementById('duration').value,'date':document.getElementById('date').value,'priority':document.getElementById('priority').value}};
     // tasks.push(task); 
      document.getElementById('title_id'+id).innerHTML=task.details.title;
      document.getElementById('task-form').style.display='none';
      console.log(task);
      console.log('done');
      tasks.push('task'+id);
      localStorage.setItem('task'+id,JSON.stringify(task));
      id++;
      saveData();
      checkTaskDurations();
}
function deleteTask(event,div_id){
    document.getElementById('div_id'+getId(div_id)).remove();
    let id1=getId(div_id);
    removeTask(id1);
    localStorage.removeItem('task'+id1);
    checkTaskDurations();
}
function removeTask(t_id){
    let idx=tasks.indexOf('task'+t_id);
    tasks.splice(idx,1);
    if(subt_parent['task'+t_id]!=null){
        delete subt_parent['task'+t_id];
    }
    saveData();
}
function hideForm(){
     document.getElementById('task-form').style.display='none';
}
function getId(name){
    return parseInt(name.substring(4,name.length));
}
function viewTask(event,btnid){
    
     event.preventDefault();
     var id1=getId(btnid);
     console.log(id1+" "+btnid);
     let item=localStorage.getItem('task'+id1);
     let task=JSON.parse(item);
     console.log(task);
     const desc=document.createElement('p');
     desc.id='desc'+id1;
     desc.textContent='Description : '+task.details.description;
     desc.style.display='block';
     const time=document.createElement('p');
     time.id='time'+id1;
     time.textContent='Duration : '+task.details.duration+" "+task.details.date;
     const  priority=document.createElement('p');
     priority.id='prio'+id1;
     console.log(desc.id+' '+time.id+' '+priority.id);
     priority.textContent='priority : '+task.details.priority;
   
     document.getElementById('col'+getId(btnid)).appendChild(desc);
     document.getElementById('col'+getId(btnid)).appendChild(time);
     document.getElementById('col'+getId(btnid)).appendChild(priority);
     
     document.getElementById('edit'+id1).style.height='50px';
     document.getElementById('bttn'+id1).style.height='50px';
     document.getElementById('view'+id1).style.height='50px';
     document.getElementById('subt'+id1).style.height='50px';
}

function createTaskCard(task_id){
            if(task_id<0){
            const mainDiv = document.createElement('div');
            mainDiv.className = 'col-md-12 mb-1 bg-light border rounded';
            mainDiv.id='div_id'+id;
            const rowDiv = document.createElement('div');
            rowDiv.className = 'row draggable';
            mainDiv.draggable = true;
            const leftCol = document.createElement('div');
            leftCol.className = 'col-md-6';
            leftCol.id = 'col'+id;
            const innerRow = document.createElement('div');
            innerRow.className = 'row d-flex';
            const checkboxCol = document.createElement('div');
            checkboxCol.className = 'col-md-1 mt-4 mx-4';
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.id='chkb'+id;
            checkbox.onchange=function(){
                strikeTask(checkbox.id);
            }
            checkboxCol.appendChild(checkbox);          
            const textCol = document.createElement('div');
            textCol.className = 'col-md-6';
            const heading = document.createElement('h3');
            heading.id='title_id'+id;
            heading.className = 'm-4';
            textCol.appendChild(heading); 
            innerRow.appendChild(checkboxCol);
            innerRow.appendChild(textCol);
            leftCol.appendChild(innerRow);
            const rightCol = document.createElement('div');
            rightCol.className = 'col-md-6 d-flex justify-content-end';
            const buttonEdit = document.createElement('button');
            buttonEdit.className = 'btn btn-dark m-3';
            buttonEdit.id='edit'+id;
            buttonEdit.textContent = 'Edit';
            buttonEdit.onclick=function(event){
               // console.log(document.getElementById('desc'+getId(buttonEdit.id)).value);
                       openDialog(event,buttonEdit.id);  
            }
            const buttonView = document.createElement('button');
            buttonView.className = 'btn btn-dark m-3';
            buttonView.id='view'+id;
            buttonView.textContent = 'View';
            buttonView.onclick = function(event){
                
                viewTask(event,buttonView.id);
            };

            const buttonDelete = document.createElement('button');
            buttonDelete.className = 'btn btn-dark m-3';
            buttonDelete.id='bttn'+id;
            buttonDelete.textContent = 'Delete';
            buttonDelete.onclick=function(event){
                deleteTask(event,buttonDelete.id);
            };

            const subTaskbtn = document.createElement('button');
            subTaskbtn.className = 'btn btn-dark m-3';
            subTaskbtn.id='subt'+id;
            subTaskbtn.textContent = 'Subtask';
            subTaskbtn.onclick=function(event){
                addsubTask(event,subTaskbtn.id);
            };
            rightCol.appendChild(buttonEdit);
            rightCol.appendChild(buttonView);
            rightCol.appendChild(buttonDelete);
            rightCol.appendChild(subTaskbtn);
            rowDiv.appendChild(leftCol);
            rowDiv.appendChild(rightCol);
            mainDiv.appendChild(rowDiv);
            if(task_id==-1){
               document.getElementById('task-container').appendChild(mainDiv);
            }
            else{
                document.getElementById('div_id'+subtaskId).appendChild(mainDiv);
            }
        }
        else{
            let task_card=JSON.parse(localStorage.getItem('task'+task_id));
            console.log(task_card);
            const mainDiv = document.createElement('div');
            mainDiv.className = 'col-md-12 mb-1 bg-light border rounded border-dark';
            mainDiv.id='div_id'+task_id;
            const rowDiv = document.createElement('div');
            rowDiv.className = 'row draggable';
            mainDiv.draggable = true;
            const leftCol = document.createElement('div');
            leftCol.className = 'col-md-6';
            leftCol.id = 'col'+task_id;      
            const innerRow = document.createElement('div');
            innerRow.className = 'row d-flex';
            const checkboxCol = document.createElement('div');
            checkboxCol.className = 'col-md-1 mt-4 mx-4';
            const checkbox = document.createElement('input');
            checkbox.id='chkb'+task_id;
            checkbox.type = 'checkbox';
            checkbox.onchange=function(event){
                 strikeTask(checkbox.id);
            }
            checkboxCol.appendChild(checkbox);
            const textCol = document.createElement('div');
            textCol.className = 'col-md-6';
            const heading = document.createElement('h3');
            heading.id='title_id'+task_id;
            heading.textContent=task_card.details.title;
            heading.className = 'm-4';
            textCol.appendChild(heading);
            innerRow.appendChild(checkboxCol);
            innerRow.appendChild(textCol);           
            leftCol.appendChild(innerRow);
            const rightCol = document.createElement('div');
            rightCol.className = 'col-md-6 d-md-flex flex-md-row d-sm-flex flex-sm-column justify-content-end';           
            const buttonEdit = document.createElement('button');
            buttonEdit.className = 'btn btn-dark  mx-1 my-4';
            buttonEdit.id='edit'+task_id;
            buttonEdit.textContent = 'Edit';
            buttonEdit.onclick=function(event){
                openDialog(event,buttonEdit.id);
            }
            const buttonView = document.createElement('button');
            buttonView.className = 'btn btn-dark  mx-1 my-4';
            buttonView.id='view'+task_id;
            buttonView.textContent = 'View';
            buttonView.onclick = function(event){
                viewTask(event,buttonView.id);
            };
            const buttonDelete = document.createElement('button');
            buttonDelete.className = 'btn btn-dark text-center  mx-1 my-4';
            buttonDelete.id='bttn'+task_id;
            buttonDelete.textContent = 'Delete';
            buttonDelete.onclick=function(event){
                deleteTask(event,buttonDelete.id);
            };
            const subTaskbtn = document.createElement('button');
            subTaskbtn.className = 'btn btn-dark mx-1 my-4  text-center';
            subTaskbtn.id='subt'+task_id;
            subTaskbtn.textContent = 'Subtask';
            subTaskbtn.onclick=function(event){
                addsubTask(event,subTaskbtn.id);
            };
            rightCol.appendChild(buttonEdit);
            rightCol.appendChild(buttonView);
            rightCol.appendChild(buttonDelete);
            rightCol.appendChild(subTaskbtn);
            rowDiv.appendChild(leftCol);
            rowDiv.appendChild(rightCol);         
            mainDiv.appendChild(rowDiv);
            if(subt_parent['task'+task_id]!=null) {
                  document.getElementById('div_id'+getId(subt_parent['task'+task_id])).appendChild(mainDiv);
            }
            else document.getElementById('task-container').appendChild(mainDiv);
        }        
}
function strikeTask(id1){
    if(document.getElementById('chkb'+getId(id1)).checked==true){
        document.getElementById('title_id'+getId(id1)).style.textDecoration='line-through';
    }
    else{
        document.getElementById('title_id'+getId(id1)).style.textDecoration='none';
    }
}

document.addEventListener('DOMContentLoaded',hideForm);
function saveData(){
    localStorage.setItem('task-cards',JSON.stringify(tasks))
    localStorage.setItem('id',id);
    localStorage.setItem('subt_parent',JSON.stringify(subt_parent));
}
function getData(){
    task_cards=JSON.parse(localStorage.getItem('task-cards'));
    subt_parent=JSON.parse(localStorage.getItem('subt_parent'));
    if(subt_parent===null) subt_parent={};
    id=JSON.parse(localStorage.getItem('id'));
    if(id==null) id=0; 
    console.log(task_cards);
    if(task_cards!=null){
        tasks=task_cards;
        for(let i=0;i<task_cards.length;i++){
           createTaskCard(getId(task_cards[i]));
        }
    }
    checkTaskDurations();
    
   //document.getElementById('task-container').innerHTML=localStorage.getItem('task-container');
   
}
document.addEventListener('DOMContentLoaded',()=>{
    getData();
    document.getElementById('sortoption').value='priority';
    sortCards();
});

//dialogue box
let edit_id=-1
function openDialog(event,e_id) {
    if(e_id==-1){
        document.getElementById('dialog').classList.add('show');
        document.getElementById('overlay').classList.add('show');
    }
    else{
        if(document.getElementById('desc'+getId(e_id))===null){
            alert('First view the task and then Click edit');
        }
        else{
        document.getElementById('dialog').classList.add('show');
        document.getElementById('overlay').classList.add('show');
        edit_id=e_id;
        }
    }
    
}


function closeDialog() {
    document.getElementById('dialog').classList.remove('show');
    document.getElementById('overlay').classList.remove('show');
}

function submitForm() {
    if(subtaskId<0){
        const title = document.getElementById('d_title').value;
        const description = document.getElementById('d_description').value;
        const time=document.getElementById('d_duration').value;
        const date=document.getElementById('d_date').value;
        const priority = document.getElementById('d_priority').value;
        const id1=getId(edit_id);
        console.log(id1+' '+time+' '+title+' '+priority+' '+description);
        document.getElementById('title_id'+id1).innerText=title;
        document.getElementById('desc'+id1).innerText='description : '+description;
        document.getElementById('time'+id1).innerText='duration: '+time+' date: '+date;
        document.getElementById('prio'+id1).innerText='priority :'+priority;
        let task={'id':id1,'details':{'title':title,'description':description,'duration':time,'date':date,'priority':priority}};
        localStorage.setItem('task'+id1,JSON.stringify(task));
    }
    else{
        const title = document.getElementById('d_title').value;
        const description = document.getElementById('d_description').value;
        const time=document.getElementById('d_duration').value;
        const date=document.getElementById('d_date').value;
        const priority = document.getElementById('d_priority').value;
        console.log(id+' '+time+' '+title+' '+priority+' '+description);
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

//theme change
var theme=1;
function changeTheme() {
    if(theme==1){
     document.getElementById('app-container').style.backgroundColor='black';
     document.getElementById('body_bg').style.backgroundColor='black'
     document.getElementById('heading1').className='mx-4 text-white';
     document.getElementById('t_title').className='mx-4 text-white';
     document.getElementById('import_s').className='text-white';
     theme=0;
    }
    else{
        document.getElementById('app-container').style.backgroundColor='rgb(250, 250, 247)';
        document.getElementById('body_bg').style.backgroundColor='white';
        document.getElementById('heading1').className='mx-4 text-dark';
        document.getElementById('t_title').className='mx-4 text-dark';
        document.getElementById('import_s').className='text-black';
        theme=1;
    }
}
document.addEventListener('DOMContentLoaded',changeTheme);


//drag and drop
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

//sorting the card 
function sortCards() {
    const taskContainer = document.getElementById('task-container');
    const taskCards = Array.from(taskContainer.children);
    const priorityOrder = { 'low': 1, 'medium': 2, 'high': 3 };
    const sortOption = document.getElementById('sortoption').value;
    if (sortOption === 'priority') {
        taskCards.sort((a, b) => {
            const id1 = getId(a.id.substring(2));
            const id2 = getId(b.id.substring(2));
            const task1 = JSON.parse(localStorage.getItem('task' + id1));
            const task2 = JSON.parse(localStorage.getItem('task' + id2));
            const priorityA = priorityOrder[task1.details.priority];
            const priorityB = priorityOrder[task2.details.priority];
            if (priorityB != priorityA)
                return priorityB - priorityA; 
            return id1-id2; 
        });
    } else if (sortOption === 'time') {
        taskCards.sort((a, b) => {
            const id1 = getId(a.id.substring(2));
            const id2 = getId(b.id.substring(2));
            const task1 = JSON.parse(localStorage.getItem('task' + id1));
            const task2 = JSON.parse(localStorage.getItem('task' + id2));
            const endTimeA = new Date(task1.details.date + 'T' + task1.details.duration);
            const endTimeB = new Date(task2.details.date + 'T' + task2.details.duration);
            const now = new Date();
            const durationA = (endTimeA - now) / (1000 * 60);
            const durationB = (endTimeB - now) / (1000 * 60);
            if (durationA !== durationB)
                return durationA - durationB; 
            return idA - idB; 
        });
    }
    taskContainer.innerHTML = '';
    taskCards.forEach(taskElement => {
        taskContainer.appendChild(taskElement);
    });
    console.log('sort done');
}
function search() {
    const query = document.getElementById('search').value.toLowerCase();
    const taskContainer = document.getElementById('task-container');
    const taskCards = Array.from(taskContainer.children);
    if (query === '') {
        taskCards.forEach(task => {
              // task.style.display = 'block';
             const  subtasks=Array.from(task.children);
            subtasks.forEach((subtask)=>{
                      if(subtask.id!='') {
                       subtask.className='col-md-12 mb-1 bg-light border rounded border-dark';
                       //subtask.style.display='block';
                      }
            });
            task.className='col-md-12 mb-1 bg-light border rounded border-dark'

        });
    } else {
        taskCards.forEach(taskCard => 
            {
              const  subtasks=Array.from(taskCard.children);
              subtasks.forEach((subtask)=>{
                          if(subtask.id!='') displayBlock(subtask,query); 
                });
               displayBlock(taskCard,query);
        });
    }
}
function displayBlock(taskCard,query){
                // console.log(query+' '+taskCard.id);
                  const id = getId(taskCard.id.substring(2));
                   const task = JSON.parse(localStorage.getItem('task' + id));
                    const title = task.details.title.toLowerCase();
                    const description = task.details.description.toLowerCase();
                    if (title.includes(query) || description.includes(query)) {
  
                        // if(subt_parent['task'+id]!=null){
                        //     document.getElementById('div_id'+getId(subt_parent['task'+id])).style.display='block';
                        // }
                       // taskCard.style.display = 'block';
                        taskCard.className='col-md-12 mb-1 bg-warning border rounded border-dark';
                    } else {
                       //taskCard.style.display = 'none';
                        taskCard.className='col-md-12 mb-1 bg-light border rounded border-dark'                        
                    }
            
}


//notifications

function requestNotificationPermission() {
    if (Notification.permission === 'default') {
        Notification.requestPermission().then(permission => {
            if (permission === 'granted') {
                console.log('Notification permission granted.');
            } else {
                console.log('Notification permission denied.');
            }
        });
    }
}

document.addEventListener('DOMContentLoaded', ()=>{
    requestNotificationPermission();
    checkTaskDurations();

});

function checkTaskDurations() {
    const now = new Date();
    tasks.forEach(taskId => {
        const task = JSON.parse(localStorage.getItem(taskId));
        if (task) {
            const endTime = new Date(task.details.date + 'T' + task.details.duration);
            const timeDiff = (endTime - now) / (1000 * 60 * 60); 
            console.log(timeDiff);
            //if(timeDiff<=0) strikeTask(taskId);
            if (timeDiff < 1) { 
                 console.log(taskId);
                sendNotification(task.details.title, task.details.description);
            }
        }
    });
    
}
function sendNotification(title, description) {
    if (Notification.permission === 'granted') {
        const options = {
            body: description,
            icon: 'path/to/icon.png'
        };

        new Notification(title, options);
    }
}


//subtask

function addsubTask(event,t_id){
    console.log(t_id);
    subtaskId=getId(t_id);
    createTaskCard(-2);
    openDialog(event,-1);  
}

//import and export

function exportData(){
    const  taskIds=localStorage.getItem('task-cards');
    const   id=localStorage.getItem('id');
    const taskObj={};
    const taskIdsArray=JSON.parse(taskIds || '[]');
    taskIdsArray.forEach(task=>{
        taskObj[task]=JSON.parse(localStorage.getItem(task));
    });
    const stordeData={
        'task-cards':taskIdsArray,
        ...taskObj,
        'id':id,
        'subt_parent' : JSON.parse(localStorage.getItem('subt_parent'))
    };
    const jsonformat=JSON.stringify(stordeData,null,2);
    const blob=new Blob([jsonformat],{type:'application/json'});
    saveAs(blob,'data.json');
    localStorage.clear();
}


function importData(event) {
    const file = document.getElementById('fileInput').files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = function(e) {
        const data = JSON.parse(e.target.result);
        localStorage.clear();
        console.log(data);
        data['task-cards'].forEach(taskId => {
          localStorage.setItem(taskId, JSON.stringify(data[taskId]));
        });
        localStorage.setItem('subt_parent', JSON.stringify(data['subt_parent']));
        localStorage.setItem('id',data['id']);
        localStorage.setItem('task-cards', JSON.stringify(data['task-cards']));
      };
      reader.readAsText(file);
    }
    console.log('import done');
};