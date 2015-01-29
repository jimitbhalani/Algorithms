using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class AstarRevised : MonoBehaviour
{
    public GameObject[] spawners;
    public GameObject plane;
    public GameObject nodeGraphic;
    public GameObject wallGraphic;
    public GameObject highWallGraphic;
    public GameObject gumba;
    public GameObject drone;
    public GameObject characterToChase;
    public Transform startOfGridRow;
    public GuiScript guiScript;
    public  int noOfChasers;
    public  float nodeXOffset = 3.0f;
    public  float nodeZOffset = 3.0f;
    private float rowSize;
    private float colSize;
    private float rows;
    private float cols;
    private float yPos;
	private float delay;
    private float currTime;
	private bool isGenerated = true;
    private Node[,] Grid;
    private List<Node> path = new List<Node>();
    private Node playerGoalNode;
    private Node playerStartNode;
    private Node targetNode;
    private Color resetNodeColor;
    private Player[] players;
    private string targetNodeName;
    private Dictionary<string, Node> NodeDictionary = new Dictionary<string, Node>();
    private CharacterGridTraverse characterScript;

    void Start()
    {
        rows = plane.renderer.bounds.size.z;
        cols = plane.renderer.bounds.size.x;
        yPos = plane.renderer.bounds.size.y;
        rowSize = (int)rows;
        colSize = (int)cols;
        Grid = new Node[(int)rowSize, (int)colSize];
        InitializeGrid();
        currTime = Time.time;
        resetNodeColor = nodeGraphic.renderer.material.color;
        spawners = GameObject.FindGameObjectsWithTag("spawner");
        delay = 0.1f;
        noOfChasers = 10;
        players = new Player[noOfChasers];
        characterScript = characterToChase.GetComponent<CharacterGridTraverse>();
        foreach (Node node in Grid)
        {
            NodeDictionary.Add(node.getNodeName(), node);
        }

        for (int i = 0; i < players.Length; i++)
        {
            float randSpeed = Random.Range(4.0f, 6.5f);
            players[i] = new Player(drone, spawners[i].transform.position, "player-" + i, randSpeed);
            playerStartNode = getNearestNode(players[i]);
            players[i].setStartNode(playerStartNode);
            pathColor(players[i], i);
        }
        preComputeNodeEgdes();
    }

    private void gridOn()
    {
        foreach (Node node in Grid)
        {
            if (node.getNodeType() != "obstacle")
            {
                node.rendererOn();
            }
        }
    }

    private void gridoff()
    {
        foreach (Node node in Grid)
        {
            if (node.getNodeType() != "obstacle")
            {
                node.rendererOff();
            }
        }
    }

    private Node getNearestNode(Player p)
    {      
        Node nearestNode = null;
        float nearestDistance = Mathf.Infinity;
        foreach(Node node in Grid)
        {
            if (node.getNodeType() != "obstacle")
            {
                float distance = Vector3.Distance(node.getNodeGraphic().transform.position, p.getPlayerGraphic().transform.position);
                if (distance < nearestDistance)
                {
                    nearestDistance = distance;
                    nearestNode = node;
                }
            }
        }
        return nearestNode;
    }

    private void pathColor(Player p,int num)
    {
        switch (num)
        {
            case 0:
                p.setPathColor(Color.blue);
                break;
            case 1:
                p.setPathColor(Color.cyan);
                break;
            case 2:
                p.setPathColor(Color.yellow);
                break;
            case 3:
                p.setPathColor(Color.green);
                break;
            case 4:
                p.setPathColor(Color.black);
                break;
            case 5:
                p.setPathColor(Color.magenta);
                break;
            case 6:
                p.setPathColor((Color.red + Color.blue));
                break;
            case 7:
                p.setPathColor((Color.cyan + Color.green));
                break;
            case 8:
                p.setPathColor((Color.yellow + Color.magenta));
                break;
            case 9:
                p.setPathColor((Color.green + Color.yellow));
                break;
        }
    }

    private void InitializeGrid()
    {
        for (int i = 0; i < rowSize; i++)
        {
            for (int j = 0; j < colSize; j++)
            {
                if ((j%2==0 && (i == 24 || i == 19 || i == 14 || i == 10 || i == 1 || i == 5)) || ( j == 0 || j == colSize-1))
                {
                     GameObject Obstacle = Instantiate(wallGraphic, new Vector3(startOfGridRow.transform.position.x + j * nodeXOffset, yPos + 0.2f, startOfGridRow.transform.position.z + i * nodeZOffset), Quaternion.identity) as GameObject;
                     Obstacle.name = (i + "-" + j + "O");
                     Grid[i, j] = new Node(Obstacle.name, Obstacle, new List<Edge>(), i, j, "obstacle");
                    
                }
                else
                {
                    GameObject nonObstacle = Instantiate(nodeGraphic, new Vector3(startOfGridRow.transform.position.x + j * nodeXOffset, yPos + 0.5f, startOfGridRow.transform.position.z + i * nodeZOffset), Quaternion.identity) as GameObject;
                    nonObstacle.name = (i + "-" + j);
                    Grid[i, j] = new Node(nonObstacle.name, nonObstacle, new List<Edge>(), i, j, "nonObstacle");
                }
            }
        }
    }

    private List<Node> GetNeighbors(Node node)
    {
        int rowIndex = 0;
        int colIndex = 0;
        List<Node> neighbors = new List<Node>();
        for (int i = 0; i < rowSize; i++)
        {
            for (int j = 0; j < colSize; j++)
            {
                if (Grid[i, j] == node)
                {
                    rowIndex = i;
                    colIndex = j;
                    break;
                }
            }
        }

        //get top neighbor
        if (rowIndex - 1 >= 0)
        {
            neighbors.Add(Grid[rowIndex - 1, colIndex]);
        }

        //get bottom neighbor
        if (rowIndex + 1 < rowSize)
        {
            neighbors.Add(Grid[rowIndex + 1, colIndex]);
        }

        //get left neighbor
        if (colIndex + 1 < colSize)
        {
            neighbors.Add(Grid[rowIndex, colIndex + 1]);
        }

        //get right neighbor
        if (colIndex - 1 >= 0)
        {
            neighbors.Add(Grid[rowIndex, colIndex - 1]);
        }
        return neighbors;
    }

    private void preComputeNodeEgdes()
    {
        //go through each and every node in the graph and get its neighbors and compute edge costs for each of them, runs o(n2) times but is amortized because we don't have more than 4 edges a node
        for (int i = 0; i < (int)rowSize; i++)
        {
            for (int j = 0; j < (int)colSize; j++)
            {
                foreach (Node neighbor in GetNeighbors(Grid[i, j]))
                {
                    if (neighbor.getNodeGraphic().gameObject.tag == "node")
                    {
                        Grid[i, j].AddEgdes(neighbor, 1);
                    }
                    else if (neighbor.getNodeGraphic().gameObject.tag == "wall")
                    {
                        Grid[i, j].AddEgdes(neighbor, 1000);
                    }
                }
            }
        }
    }

    /*
     *   ******************************************************************* ASTAR ******************************************************************
       * Have a sorted array that is the fronteir to fetch minimum cost node from the beginning and add nodes to the end
       * Have a parent dictionary to trace back the path
       * Have a distance dictionary to update all the new distances
       * for all neighbors of current fetched from fronteir front -> calculate new cost = cost of current(parent) in distance array + cost of edge
       * if the cost is less the cost of the neighbor in distance array then update the distance of the neighbor in distance array
       * calculate the manhattan distance of current node to the goal node and update the priority before sorting the fronteir
       * add the neighbor to fronteir and sort the fronteir array ( could have used a Priority Queue but C# doesn't provide you with one and writing one was not feasible )
       */

    private List<Node> Astar(Node startNode, Node goalNode, Color pathColor)
    {
        if (goalNode == null || startNode == null)
            return null;

        List<Edge> frontier = new List<Edge>();
        Dictionary<Node, Node> parentDict = new Dictionary<Node, Node>();
        Dictionary<Node, float> distances = new Dictionary<Node, float>();
        List<Node> pathStack = new List<Node>();
        frontier.Add(new Edge(null, 0));
        parentDict[startNode] = null;

        //initialize distances
        foreach (Node node in Grid)
        {
            distances[node] = Mathf.Infinity;
        }
        distances[startNode] = 0;

        while (frontier.Count > 0)
        {
            Node current = null;

            //this statement runs only once at the beginning to get the start node
            if (frontier[0].getDestination() == null)
            {
                current = startNode;
            }
            //always get the current from fronteir first
            else
            {
                current = frontier[0].getDestination();              
            }

            //if we reach our goal node break the loop
            if (current == goalNode)
                break;

            //we remove the first node from fronteir every time so that after each sort we get the next minimum edge node at 0th position
            frontier.RemoveAt(0);

            List<Edge> nodeEdges = current.getEdges();       //get all the edges
            foreach (Edge neighborEdge in nodeEdges)
            {
                float new_distance = distances[current] + neighborEdge.getCost();
                if (!(parentDict.ContainsKey(neighborEdge.getDestination())) || new_distance < distances[neighborEdge.getDestination()])
                {
                    //parents and distances should be updated everytime if one of the above condition is true
                    distances[neighborEdge.getDestination()] = new_distance;
                    parentDict[neighborEdge.getDestination()] = current;
                    float priority = new_distance + Manhattan(neighborEdge.getDestination(), goalNode);
                    neighborEdge.addToCost(priority);
                    //each node is only scanned once if we pick the minimum cost node from the fronteir
                    frontier.Add(neighborEdge);
                }
            }

            //Sort the fronteir based on minimum costs of edges, most important step
            frontier.Sort();
        }

        //finally return the path tree
        Node newCurrent = goalNode;
        while (startNode != newCurrent)
        {
            pathStack.Add(newCurrent);
            if (newCurrent.getNodeType() != "obstacle")
            {
                newCurrent.getNodeGraphic().renderer.material.color = pathColor;
            }
            newCurrent = parentDict[newCurrent];
        }
        goalNode.getNodeGraphic().renderer.material.color = Color.red;
        if (newCurrent.getNodeType() != "obstacle")
        {
            newCurrent.getNodeGraphic().renderer.material.color = pathColor;
        }
        pathStack.Add(startNode);
        return pathStack;
    }

    private float Manhattan(Node currNode, Node goalNode)
    {
        return (Mathf.Abs(currNode.getNodeRow() - goalNode.getNodeRow()) + Mathf.Abs(currNode.getNodeCol() - goalNode.getNodeCol()));
    }

    private void getNodeEdgeInformation()
    {
        for (int i = 0; i < (int)rowSize; i++)
        {
            for (int j = 0; j < (int)colSize; j++)
            {
                foreach (Edge edge in Grid[i, j].getEdges())
                {
                    Debug.Log(" FOR NODE " + Grid[i, j].getNodeName() + " EDGE IS " + edge.getDestination().getNodeName() + " with cost " + edge.getPriority());
                }
            }
        }
    }

	private void followPath(List<Node> path, Player player)
    {
        if (path == null || path.Count <= 0)
            return;

        targetNode = path[path.Count - 1];
        player.setStartNode(targetNode); 
        //seek the target node

        player.getPlayerGraphic().gameObject.transform.LookAt(targetNode.getNodeGraphic().transform.position);
        player.getPlayerGraphic().gameObject.transform.Translate(Vector3.forward * Time.deltaTime * player.getSpeed());

        if (Vector3.Distance(player.getPlayerGraphic().gameObject.transform.position, targetNode.getNodeGraphic().transform.position) < 1.5f)
        {
            path.Remove(targetNode);
        }
    }

    private void resetAllNodesColor()
    {
        for (int i = 0; i < rowSize; i++)
        {
            for (int j = 0; j < colSize; j++)
            {
                if (Grid[i,j].getNodeType() != "obstacle")
                {
                    Grid[i, j].getNodeGraphic().renderer.material.color = Color.blue / 0.5f;
                }
            }
        }
    }
	
    void Update()
    {
        if (guiScript.showGrid)
        {
            gridOn();
        }
        else
        {
            gridoff();
        }

        if (currTime + delay < Time.time)
        {
            resetAllNodesColor();
            currTime = Time.time;
            if (!(characterScript.getCharacterNodeName() == null))
            {
                targetNodeName = characterScript.getCharacterNodeName();
            }
            if (targetNodeName != null)
            {
                playerGoalNode = NodeDictionary[targetNodeName];
            }
            foreach (Player p in players)
            {
                p.setGoalNode(playerGoalNode);
                path = Astar(p.getStartNode(), p.getGoalNode(),p.getPathColor());
                if (path != null)
                {
                    //set idle animation for our drone here
                    if (path.Count == 1)
                    {
                        p.getPlayerGraphic().GetComponent<DroneControl>().setIdle = true;
                    }
                    else
                    {
                        p.getPlayerGraphic().GetComponent<DroneControl>().setIdle = false;
                    }
                }
                p.setPlayerPath(path);
            }
        }

        foreach (Player p in players)
        {
            followPath(p.getPath(), p);
        }
    }
}
