import ReactDOM from "react-dom/client";

const modalstyle= {
    display: "table",
    position:  "absolute" as const,
    top: "50%",
    left:"50%",
    transform: "-50%-50%",
    width: "3rem",
    height: "1rem",
    marginLeft: "-1.5rem",
    marginTop: "-0.5rem",
    borderRadius: ".08rem",
    background: "rgba(0, 0,0, .7)",
}

const modalTextStyle= {
    display: "table-cell",
    padding:".2rem",
    fontSize:".16rem",
    color: "#FFF",
    verticalAlign: "middle",
    textAlign:"center" as const,
}

const element = document.createElement('div');


const root = ReactDOM.createRoot(element);

//timeout default 3000ms, can be covered by timeout parameter
export const message = (message: string, timeout=3000)=>{
    root.render(
        <div style={modalstyle}>
            <div style={modalTextStyle} >{message}</div>
        </div>,

    );
    if(!element.parentNode){
        document.body.appendChild(element);
        setTimeout(()=>{
            document.body.removeChild(element);
        },timeout);
    }


}

