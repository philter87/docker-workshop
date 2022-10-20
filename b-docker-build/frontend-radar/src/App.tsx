import Grid from '@mui/material/Grid'
import {useEffect, useState} from 'react'
import './App.css'
import Typography from "@mui/material/Typography";
import {Ship} from "./ship";

const failed = "failed!"

function App() {
    const [ships, setShips] = useState([] as Ship[])
    const [url, setUrl] = useState(failed);
    const [lastFetch, setLastFetch] = useState("");
    const shipsInOrder = ships.sort(s => s.points);
    const shipsByXy: { [xy: string]: Ship } = {};
    for (const ship of ships) {
        const xy = ship.x + "," + ship.y;
        shipsByXy[xy] = ship;
    }

    async function fetchAndSetShips(baseUrl: string) {
        if (failed == baseUrl) {
            return;
        }
        try {
            const res = await fetch(baseUrl + "/game/score");
            const ships = await res.json() as Ship[];

            if (res.ok) {
                setUrl(baseUrl);
                setShips(ships)
            }
        } catch (e) {
        }
    }


    useEffect(() => {
        if (url == failed) {
            const hostname = location.hostname;
            fetchAndSetShips('http://' + hostname + ':8080');
            fetchAndSetShips('http://backend:8080');
            fetchAndSetShips('http://' + hostname + ':9500');
        }
        const handler = setInterval(async () => {
            await fetchAndSetShips(url);
        }, 200);

        return () => clearInterval(handler);
    }, [url]);

    const gridArray = Array.from(Array(10).keys())

    function CreateHeaderRow() {
        return gridArray.map(i => <Grid key={i} item xs={1}>{i}</Grid>);
    }

    function CreateShipPoint(x: number, y: number) {
        const currentShip = shipsByXy[x + "," + y];
        const color = currentShip == null ? 'white' : currentShip.color;
        const dotSize = !!currentShip ? <span>&#11044;</span> : <span>&middot;</span>;


        return <Grid key={x} item xs={1} sx={{color: color, cursor: 'pointer'}} onClick={() => {
            const moveOrShoot = currentShip?.displayName == 'AI' ? 'shoot' : 'move'
            const moveUrl = url + `/game/${moveOrShoot}?x=${x}&y=${y}&displayName=you`;
            fetch(moveUrl)
            setLastFetch(moveUrl)
            console.log("click", x, y, url)
        }
        }>{dotSize}</Grid>
    }

    function CreateShipRow(y: number) {
        return gridArray.map(x => CreateShipPoint(x, y));
    }

    return (
        <div style={{color: 'white'}}>
            <h1 className="title">The radar game</h1>
            <Grid container justifyContent="center">
                <Grid item xs={5}>
                    <Grid container justifyContent="center" sx={{p: 2}}>
                        <Grid item xs={1}>-</Grid>
                        {CreateHeaderRow()}
                    </Grid>

                    {gridArray.map(y =>
                        <Grid container justifyContent="center" sx={{p: 2}}>
                            <Grid key={y} xs={1} item>{y}</Grid>
                            {CreateShipRow(y)}
                        </Grid>
                    )}
                    <Grid container>
                        Score is fetched from: {url}
                    </Grid>
                    <Grid container>
                        Move endpoint: {lastFetch}
                    </Grid>
                </Grid>
                <Grid item xs={2}></Grid>

                <Grid item xs={3}>
                    <Typography variant="h5">Scoreboard</Typography>
                    <Grid container direction="column" justifyItems="center">
                        <Grid container sx={{color: 'white', pb: 1}}>
                            <Grid xs={2} item>Name</Grid>
                            <Grid xs={1} item></Grid>
                            <Grid xs={2} item>Hits</Grid>
                            <Grid xs={2} item>Moves</Grid>
                            <Grid xs={2} item>Age</Grid>
                            <Grid xs={1} item></Grid>
                            <Grid xs={2} item>Score</Grid>
                        </Grid>
                        {shipsInOrder.map(s => <ScoreRow key={s.displayName} s={s}/>)}
                    </Grid>
                </Grid>
            </Grid>
        </div>
    )
}

export const ScoreRow = ({s}: { s: Ship }) => {
    return <Grid container sx={{color: s.color}}>
        <Grid xs={2} item>{s.displayName}</Grid>
        <Grid xs={1} item></Grid>
        <Grid xs={2} item>{s.hitCount}</Grid>
        <Grid xs={2} item>{s.moveCount}</Grid>
        <Grid xs={2} item>{s.agePoints}</Grid>
        <Grid xs={1} item></Grid>
        <Grid xs={2} item>{s.points}</Grid>
    </Grid>
}

export default App
