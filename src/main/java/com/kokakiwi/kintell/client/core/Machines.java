package com.kokakiwi.kintell.client.core;

import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import com.google.common.collect.Maps;

public class Machines
{
    private final KintellClientCore    core;
    
    private final Map<String, Machine> machines = Maps.newLinkedHashMap();
    
    public Machines(KintellClientCore core)
    {
        this.core = core;
    }
    
    public KintellClientCore getCore()
    {
        return core;
    }
    
    public Map<String, Machine> getMachines()
    {
        return machines;
    }
    
    public void addMachine(Machine machine)
    {
        machines.put(machine.getId(), machine);
    }
    
    public Machine getMachine(String id)
    {
        return machines.get(id);
    }
    
    public Machine createMachine(String id)
    {
        Machine machine = new Machine(this, id);
        
        return machine;
    }
    
    public TreeModel getTreeModel()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Machines");
        
        for (Machine machine : machines.values())
        {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(
                    machine.getId());
            for (Program program : machine.getPrograms().values())
            {
                DefaultMutableTreeNode programNode = new DefaultMutableTreeNode(
                        program);
                node.add(programNode);
            }
            root.add(node);
        }
        
        TreeModel model = new DefaultTreeModel(root);
        return model;
    }
}
