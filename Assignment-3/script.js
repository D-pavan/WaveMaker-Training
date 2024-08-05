var operand1="";
var operand2="";
var memory="0";
var operator="";
screen=document.getElementById("screen");
screen.value="0";
function appendInput(digit){
    if(screen.value=="0") screen.value="";
    screen.value+=digit;
}
function plus_or_minus(){
    if(parseInt(screen.value)>0){
        screen.value=-(parseInt(screen.value));
    }
    else{
        screen.value=-(parseInt(screen.value));
    }
}
function getOperators(operator1){ 
    operand1=screen.value;
    operator=operator1;
    screen.value='';
}
function equal(){
    if(operand1==""&&operand2=="") {
        screen.value=screen.value;
    }
    else{
        try{
            operand2=screen.value;
            var inp=operand1+operator+operand2;
            var ans=eval(inp);
            screen.value=ans;
            operand1=ans;
        }
        catch(error){
            screen.value="Error";
        }
    }
}
function sqr_root(){
   if(operator!=""&&operand2==""){
        screen.value=eval(operand1+operator+Math.sqrt(parseInt(operand1)));    
    }
    else{ 
           screen.value=Math.sqrt(parseInt(screen.value));
    } 
}
function onebyx(){
    if(operator!=""&&operand2==""){
        screen.value=eval(operand1+operator+"1/"+operand1);    
    }
    else{ 
           screen.value=1/parseInt(screen.value);
    } 
}
function M_plus(){
    try{
     memory=eval(memory+"+"+screen.value);
    }
    catch(err){
        screen.value="Error";
    }
}
function M_minus(){
    try{
    memory=eval(memory+"-"+screen.value);
    }
    catch(err){
        screen.value="Error";
    }
}
function MS(){
    memory=screen.value;
}
function MC(){
    screen.value="0";
    memory="0";
    operand1="";operand2="";
}
function MR(){
    screen.value=memory;
}
function CE(){
    screen.value="0";
    operand1="";operand2="";
    operator="";
}
function back(){
    if(screen.value.length==1){
        screen.value="0";
    }
    else{
        screen.value=screen.value.substring(0,screen.value.length-1);
    }
}