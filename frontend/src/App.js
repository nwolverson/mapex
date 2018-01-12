import React, { Component } from "react";
import "./App.css";

import { Map, TileLayer, GeoJSON } from "react-leaflet";
import randomColor from "random-color";

import Drawer from "material-ui/Drawer";
import "material-ui/Reboot";
import List from "material-ui/List";
import { MenuItem } from "material-ui/Menu";
import Paper from "material-ui/Paper";
import withStyles from "material-ui/styles/withStyles";
import { CircularProgress } from "material-ui/Progress";

const drawerWidth = 300;
const styles = theme => ({
  drawerPaper: {
    width: drawerWidth
  },
  mapPaper: {
    width: `calc(100% - ${drawerWidth}px)`,
    position: "relative"
  },
  loadingOverlay: {
    position: "absolute",
    top: 0,
    left: 0,
    bottom: 0,
    right: 0,
    background: "rgba(255,255,255,0.3)",
    zIndex: 500
  }
});

class App extends Component {
  state = {
    bounds: [[55.94579, -3.20196], [55.93338, -3.2557]],
    areas: null,
    path: null,
    routes: [],
    route: null,
    loading: true
  };

  componentDidMount() {
    fetch("/activities", {
      credentials: "same-origin",
      headers: [["Accept", "application/json"]]
    })
      .then(r => r.json())
      .then(routes => {
        this.setState({ routes });
        routes.length && this.fetchRoute(routes[0].id);
      });
  }

  fetchRoute = id => {
    this.setState({ loading: true });
    fetch(`/activities/${id}`, {
      credentials: "same-origin"
    })
      .then(resp => resp.json())
      .then(json => {
        const { areas, path } = json;
        this.setState({
          areas: areas.map(JSON.parse),
          path: JSON.parse(path),
          route: id,
          loading: false,
          bounds: json.bounds
        });
      });
  };

  style = geoJsonFeature => ({ color: randomColor().hexString() });

  render() {
    const { classes } = this.props;
    return (
      <div className="App">
        <Drawer
          type="permanent"
          style={{ width: drawerWidth }}
          classes={{
            paper: classes.drawerPaper
          }}
          anchor="right"
        >
          <List>
            {this.state.routes.map(r => (
              <MenuItem value={r.id} selected={r.id === this.state.route}
              onClick={() => !this.state.loading && this.fetchRoute(r.id)}
              >
                {r.name}
              </MenuItem>
            ))}
          </List>
        </Drawer>

        <Paper depth={0} classes={{ root: classes.mapPaper }}>
          <Map
            style={{ height: "100vh", width: `calc(100%-${drawerWidth}px` }}
            
            bounds={this.state.bounds||undefined}
          >
            <TileLayer
              attribution="&amp;copy <a href=&quot;http://osm.org/copyright&quot;>OpenStreetMap</a> contributors"
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            {this.state.areas &&
              this.state.route && (
                <GeoJSON
                  key={"A:" + this.state.route}
                  data={this.state.areas} 
                  style={this.style}
                />
              )}
            {this.state.path &&
              this.state.route && (
                <GeoJSON key={"P:" + this.state.route} data={this.state.path} />
              )}
          </Map>
          {this.state.loading && <div className={classes.loadingOverlay}>
            <CircularProgress size={50} style={{marginTop: "calc(50% - 50px)"}} />
          </div>
          }
        </Paper>
      </div>
    );
  }
}

export default withStyles(styles)(App);
