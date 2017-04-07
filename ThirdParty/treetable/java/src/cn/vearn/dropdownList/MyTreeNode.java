/**
 * @File MyTreeNode.java
 * @author zeng
 * @Date 2017-03-29
 * @Time 09:23
 */

package cn.vearn.dropdownList;

import java.util.ArrayList;
import java.util.List;

public class MyTreeNode {
    private String name;
    private String description;
    private List<MyTreeNode> children = new ArrayList<MyTreeNode>();

    public MyTreeNode()
    {
    }

    public MyTreeNode( String name, String description )
    {
        this.name = name;
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<MyTreeNode> getChildren()
    {
        return children;
    }

    public String toString()
    {
        return "MyTreeNode: " + name + ", " + description;
    }
}
