package com.ecity.cswatersupply.ui.widght.treeview.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.OrganisationSelection;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.ui.widght.treeview.annotation.TreeNodeHasSelected;
import com.ecity.cswatersupply.ui.widght.treeview.annotation.TreeNodeId;
import com.ecity.cswatersupply.ui.widght.treeview.annotation.TreeNodeIsName;
import com.ecity.cswatersupply.ui.widght.treeview.annotation.TreeNodeIsSelected;
import com.ecity.cswatersupply.ui.widght.treeview.annotation.TreeNodeLabel;
import com.ecity.cswatersupply.ui.widght.treeview.annotation.TreeNodePid;
import com.ecity.cswatersupply.ui.widght.treeview.model.TreeNode;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.workorder.model.TreeViewLocalData;

public class TreeViewHelper {

    private static boolean is3TreeView = false;
    private static TreeNode menu;

    public static TreeNode getMenu() {
        return menu;
    }

    public static void setMenu(TreeNode menu) {
        TreeViewHelper.menu = menu;
    }

    public static boolean isIs3TreeView() {
        return is3TreeView;
    }

    public static void setIs3TreeViewFalse() {
        is3TreeView = false;
    }

    /**
     * 将InspectItem类型(处理类别)的数据转为本地WorkOrderFinishHandleType类型的数据
     * @param inspectItem
     * @return
     */
    public static List<TreeViewLocalData> convertInspectItemToHandleType(InspectItem inspectItem) {
        List<TreeViewLocalData> workOrderFinishHandleTypes = new ArrayList<TreeViewLocalData>();
        if (null == inspectItem) {
            return workOrderFinishHandleTypes;
        }
        String selectValuesJson = inspectItem.getSelectValues();
        if (StringUtil.isBlank(selectValuesJson)) {
            return workOrderFinishHandleTypes;
        }
        String[] valueArrays = null;
        if (!StringUtil.isBlank(inspectItem.getValue())) {
            valueArrays = inspectItem.getValue().split(",");
        }
        try {
            JSONArray jsonArray = new JSONArray(selectValuesJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                int childAllNum = 0;
                int childSelectedNum = 0;
                TreeViewLocalData handleParentType = new TreeViewLocalData();
                JSONObject jsonParentType = jsonArray.optJSONObject(i);
                handleParentType.setId(String.valueOf(i));
                handleParentType.setAlias(jsonParentType.optString("alias"));
                handleParentType.setName(String.valueOf(jsonParentType.optString("name")));
                handleParentType.setpId("");
                if (null != valueArrays) {
                    for (String name : valueArrays) {
                        if (handleParentType.getName().equalsIgnoreCase(name)) {
                            handleParentType.setSelected(false);
                            handleParentType.setHasSelected(true);
                            break;
                        } else {
                            handleParentType.setSelected(false);
                            handleParentType.setHasSelected(false);
                        }
                    }
                } else {
                    handleParentType.setSelected(false);
                    handleParentType.setHasSelected(false);
                }
                workOrderFinishHandleTypes.add(handleParentType);
                JSONArray jsonChildType = jsonParentType.optJSONArray("selectValues");
                if (null == jsonChildType) {
                    if (handleParentType.isHasSelected()) {
                        handleParentType.setSelected(true);
                    }
                    continue;
                }
                for (int j = 0; j < jsonChildType.length(); j++) {
                    childAllNum++;
                    int childAllNum3 = 0;
                    int selectedChildNum3 = 0;
                    JSONObject jsonChildOpject = jsonChildType.optJSONObject(j);
                    TreeViewLocalData handleChildType = new TreeViewLocalData();
                    handleChildType.setId(i + "_" + j);
                    handleChildType.setName(String.valueOf(jsonChildOpject.optString("name")));
                    handleChildType.setAlias(jsonChildOpject.optString("alias"));
                    handleChildType.setpId(handleParentType.getId());
                    if (null != valueArrays) {
                        for (String name : valueArrays) {
                            if (handleChildType.getName().equalsIgnoreCase(name)) {
                                handleChildType.setHasSelected(false);
                                handleChildType.setSelected(true);
                                handleParentType.setHasSelected(true);
                                childSelectedNum++;
                                break;
                            } else {
                                handleChildType.setHasSelected(false);
                                handleChildType.setSelected(false);
                            }
                        }
                    } else {
                        handleChildType.setHasSelected(false);
                        handleChildType.setSelected(false);
                    }
                    workOrderFinishHandleTypes.add(handleChildType);

                    //3级树
                    JSONArray jsonChildType3 = jsonChildOpject.optJSONArray("selectValues");
                    if (null == jsonChildType3) {
                        if (handleChildType.isHasSelected()) {
                            handleChildType.setSelected(true);
                        }
                        continue;
                    }
                    for (int k = 0; k < jsonChildType3.length(); k++) {
                        childAllNum3++;
                        JSONObject jsonChildOpject3 = jsonChildType3.optJSONObject(k);
                        TreeViewLocalData handleChildType3 = new TreeViewLocalData();
                        handleChildType3.setId(i + "_" + j + "_" + k);
                        handleChildType3.setName(String.valueOf(jsonChildOpject3.optString("name")));
                        handleChildType3.setAlias(jsonChildOpject3.optString("alias"));
                        handleChildType3.setpId(handleChildType.getId());
                        if (null != valueArrays) {
                            for (String name : valueArrays) {
                                if (handleChildType3.getName().equalsIgnoreCase(name)) {
                                    handleChildType3.setHasSelected(false);
                                    handleChildType3.setSelected(true);
                                    handleChildType.setHasSelected(true);
                                    selectedChildNum3++;
                                    break;
                                } else {
                                    handleChildType3.setHasSelected(false);
                                    handleChildType3.setSelected(false);
                                }
                            }
                        } else {
                            handleChildType3.setHasSelected(false);
                            handleChildType3.setSelected(false);
                        }
                        workOrderFinishHandleTypes.add(handleChildType3);
                    }
                    if (selectedChildNum3 == childAllNum3) {
                        handleChildType.setSelected(true);
                    }
                    is3TreeView = true;
                    //3级树
                }

                if (childSelectedNum == childAllNum) {
                    handleParentType.setSelected(true);
                }

            }

        } catch (JSONException e) {
            LogUtil.e(workOrderFinishHandleTypes, e);
        }

        return workOrderFinishHandleTypes;
    }

    public static List<TreeViewLocalData> convertOrganisationToHandleType(OrganisationSelection inspectItem) {
        List<TreeViewLocalData> workOrderFinishHandleTypes = new ArrayList<TreeViewLocalData>();
        if (null == inspectItem) {
            return workOrderFinishHandleTypes;
        }
        String selectValuesJson = inspectItem.toString();
        JSONObject organization = JsonUtil.getJsonObject(selectValuesJson);
        JSONArray organizations = organization.optJSONArray("children");
        for (int i = 0; i < organizations.length(); i++) {
            int childAllNum = 0;
            int childSelectedNum = 0;
            TreeViewLocalData handleParentType = new TreeViewLocalData();
            JSONObject jsonParentType = organizations.optJSONObject(i);
            handleParentType.setId(String.valueOf(i)+"_"+String.valueOf(i));
            handleParentType.setAlias(jsonParentType.optString("name"));
            handleParentType.setName(String.valueOf(jsonParentType.optString("code")));
            workOrderFinishHandleTypes.add(handleParentType);
            JSONArray jsonChildType = jsonParentType.optJSONArray("children");
            for (int j = 0; j < jsonChildType.length(); j++) {
                childAllNum++;
                int childAllNum3 = 0;
                int selectedChildNum3 = 0;
                JSONObject jsonChildOpject = jsonChildType.optJSONObject(j);
                TreeViewLocalData handleChildType = new TreeViewLocalData();
                handleChildType.setId(String.valueOf(i)+"_"+String.valueOf(i) + "_" + j);
                handleChildType.setName(String.valueOf(jsonChildOpject.optString("code")));
                handleChildType.setAlias(jsonChildOpject.optString("name"));
                handleChildType.setpId(handleParentType.getId());
                workOrderFinishHandleTypes.add(handleChildType);

                JSONArray jsonChildType3 = jsonChildOpject.optJSONArray("children");
                if (null == jsonChildType3) {
                    if (handleChildType.isHasSelected()) {
                        handleChildType.setSelected(true);
                    }
                    continue;
                }
                for (int k = 0; k < jsonChildType3.length(); k++) {
                    childAllNum3++;
                    JSONObject jsonChildOpject3 = jsonChildType3.optJSONObject(k);
                    TreeViewLocalData handleChildType3 = new TreeViewLocalData();
                    handleChildType3.setId(String.valueOf(i)+"_"+String.valueOf(i) + "_" + j + "_" + k);
                    handleChildType3.setName(String.valueOf(jsonChildOpject3.optString("code")));
                    handleChildType3.setAlias(jsonChildOpject3.optString("name"));
                    handleChildType3.setpId(handleChildType.getId());
                    workOrderFinishHandleTypes.add(handleChildType3);
                }
                if (selectedChildNum3 == childAllNum3) {
                    handleChildType.setSelected(true);
                }
                is3TreeView = true;
                //3级树
            }

            if (childSelectedNum == childAllNum) {
                handleParentType.setSelected(true);
            }

        }
        return workOrderFinishHandleTypes;
    }

    /**
     * 将用户的数据转为树型数据
     * @param datas
     * @return
     */
    public static <T> List<TreeNode> converDatas2Nodes(List<T> datas) {
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        String id = "-1";
        String pid = "-1";
        String name = "";
        boolean isSelected = false;
        boolean hasSelected = false;
        for (T data : datas) {
            Class<? extends Object> clazz = data.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    if (null != field.getAnnotation(TreeNodeId.class)) {
                        id = (String) field.get(data);
                    } else if (null != field.getAnnotation(TreeNodePid.class)) {
                        pid = (String) field.get(data);
                    } else if (null != field.getAnnotation(TreeNodeLabel.class)) {
                        name = (String) field.get(data);
                    } else if (null != field.getAnnotation(TreeNodeIsSelected.class)) {
                        isSelected = field.getBoolean(data);
                    } else if (null != field.getAnnotation(TreeNodeHasSelected.class)) {
                        hasSelected = field.getBoolean(data);
                    }
                } catch (Exception e) {
                    LogUtil.e(treeNodes, e);
                }
            }

            TreeNode treeNode = new TreeNode();
            treeNode.setId(id);
            treeNode.setPid(pid);
            treeNode.setName(name);
            treeNode.setExpand(false);
            treeNode.setSelected(isSelected);
            treeNode.setHasSelected(hasSelected);
            treeNodes.add(treeNode);
        }

        for (int i = 0; i < treeNodes.size(); i++) {
            TreeNode pNode = treeNodes.get(i);
            for (int j = i + 1; j < treeNodes.size(); j++) {
                TreeNode cNode = treeNodes.get(j);
                if (pNode.getId().equalsIgnoreCase(cNode.getPid())) {
                    pNode.getChildren().add(cNode);
                    cNode.setParent(pNode);
                } /*else if (pNode.getPid().equalsIgnoreCase(cNode.getId())) {
                    pNode.setParent(cNode);
                    cNode.getChildren().add(pNode);
                  }*/else {
                    // no logic to do.
                }
            }
        }

        // 设置图标
        for (TreeNode node : treeNodes) {
            setNodeIcon(node);
        }

        return treeNodes;
    }
    
    
    /**
     * 部门转换
     * @param datas
     * @return
     */
    public static <T> List<TreeNode> converDatas2ApartmentNodes(List<T> datas) {
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        String id = "-1";
        String pid = "-1";
        String name = "";
        String group = "";
        boolean isSelected = false;
        boolean hasSelected = false;
        for (T data : datas) {
            Class<? extends Object> clazz = data.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    if (null != field.getAnnotation(TreeNodeId.class)) {
                        id = (String) field.get(data);
                    } else if (null != field.getAnnotation(TreeNodePid.class)) {
                        pid = (String) field.get(data);
                    } else if (null != field.getAnnotation(TreeNodeLabel.class)) {
                        name = (String) field.get(data);
                    } else if (null != field.getAnnotation(TreeNodeIsSelected.class)) {
                        isSelected = field.getBoolean(data);
                    } else if (null != field.getAnnotation(TreeNodeHasSelected.class)) {
                        hasSelected = field.getBoolean(data);
                    } else if (null!=field.getAnnotation(TreeNodeIsName.class)){
                        group = (String) field.get(data);
                    }
                } catch (Exception e) {
                    LogUtil.e(treeNodes, e);
                }
            }

            TreeNode treeNode = new TreeNode();
            treeNode.setId(id);
            treeNode.setGroup(group);
            treeNode.setPid(pid);
            treeNode.setName(name);
            treeNode.setExpand(false);
            treeNode.setSelected(isSelected);
            treeNode.setHasSelected(hasSelected);
            treeNodes.add(treeNode);
        }

        for (int i = 0; i < treeNodes.size(); i++) {
            TreeNode pNode = treeNodes.get(i);
            for (int j = i + 1; j < treeNodes.size(); j++) {
                TreeNode cNode = treeNodes.get(j);
                if (pNode.getId().equalsIgnoreCase(cNode.getPid())) {
                    pNode.getChildren().add(cNode);
                    cNode.setParent(pNode);
                } /*else if (pNode.getPid().equalsIgnoreCase(cNode.getId())) {
                    pNode.setParent(cNode);
                    cNode.getChildren().add(pNode);
                  }*/else {
                    // no logic to do.
                }
            }
        }

        // 设置图标
        for (TreeNode node : treeNodes) {
            setNodeIcon(node);
        }

        return treeNodes;
    }


    /**
     * 设置节点之前的图片
     * 
     * @param node
     */
    public static void setNodeIcon(TreeNode node) {
        if (node.getChildren().size() > 0) {
            if (node.isExpand()) {
                node.setIcon(R.drawable.arrow_down);
            } else {
                node.setIcon(R.drawable.arrow_right);
            }
        } else {
            node.setIcon(-1);
        }
    }

    /**
     * 对源数据进行排序
     * 
     * @param lData
     *            源数据
     * @param defaultExpandLevel
     *            默认展开几层
     * @return
     * @throws Exception
     */
    public static <T> List<TreeNode> getSortedNodes(List<TreeNode> lNodes, int defaultExpandLevel) {
        List<TreeNode> lResult = new ArrayList<TreeNode>();
        //List<TreeNode> lNodes = converDatas2Nodes(lData);
        // 获取根节点
        List<TreeNode> lRootNodes = getRootNodes(lNodes);
        for (TreeNode node : lRootNodes) {
            addNode(lResult, node, defaultExpandLevel, node.getLevel());
        }
        return lResult;
    }

    public static <T> List<TreeNode> getSortedNodesThree(List<TreeNode> lNodes, int defaultExpandLevel, List<TreeNode> lMenus) {
        List<TreeNode> lResult = new ArrayList<TreeNode>();
        //List<TreeNode> lNodes = converDatas2Nodes(lData);
        // 获取根节点
        lMenus = getRootNodes(lNodes, lMenus);
        for (TreeNode t : lMenus) {
            t.setExpand(true);
        }
        List<TreeNode> lSecondNodes = getSecondNodes(lNodes);
        for (TreeNode node : lSecondNodes) {
            addNode(lResult, node, defaultExpandLevel, node.getLevel());
        }
        return lResult;
    }

    /**
     * 把一个节点的所有孩子节点都放入result
     * 
     * @param lResult
     * @param node
     * @param defaultExpandLevel
     * @param currentLevel
     */
    private static void addNode(List<TreeNode> lResult, TreeNode node, int defaultExpandLevel, int currentLevel) {
        lResult.add(node);
        node.setExpand(defaultExpandLevel >= currentLevel);

        if (!node.isLeaf()) {
            for (TreeNode bean : node.getChildren()) {
                addNode(lResult, bean, defaultExpandLevel, bean.getLevel());
            }
        }
    }

    /**
     * 获取所有根节点
     * 
     * @param lNodes
     * @return
     */
    private static List<TreeNode> getRootNodes(List<TreeNode> lNodes) {
        List<TreeNode> lRoot = new ArrayList<TreeNode>();

        for (TreeNode node : lNodes) {
            if (node.isRoot()) {
                lRoot.add(node);
            }
        }

        return lRoot;
    }

    /**
     * 获取所有根节点,并生成三级列表菜单
     * 
     * @param lNodes
     * @return
     */
    private static List<TreeNode> getRootNodes(List<TreeNode> lNodes, List<TreeNode> lMenus) {

        for (TreeNode node : lNodes) {
            if (node.isRoot()) {
                lMenus.add(node);
            }
        }

        return lMenus;
    }

    /**
     * 获取所有第二级节点
     * 
     * @param lNodes
     * @return
     */
    private static List<TreeNode> getSecondNodes(List<TreeNode> lNodes) {
        List<TreeNode> lSecondNodes = new ArrayList<TreeNode>();

        for (TreeNode node : lNodes) {
            String[] ids = node.getId().split("_");
            if (null != ids && ids.length == 2) {
                lSecondNodes.add(node);
            }
        }

        return lSecondNodes;
    }

    /**
     * Tree控件初始化时过滤出所有显示出来的节点
     * 
     * @param lDatas
     * @return
     */
    public static List<TreeNode> filterVisibleNode(List<TreeNode> lDatas) {
        List<TreeNode> lResult = new ArrayList<TreeNode>();

        for (TreeNode bean : lDatas) {
            if (bean.isRoot() || bean.isParentExpand()) {
                setNodeIcon(bean);
                lResult.add(bean);
            }
        }

        return lResult;
    }

    /**
     * Tree控件初始化过后,根据用户选择过滤出所有显示出来的节点
     * 
     * @param lDatas
     * @return
     */
    public static List<TreeNode> filterVisibleNode(List<TreeNode> lDatas, TreeNode lData) {
        if ("-1".equalsIgnoreCase(lData.getId())) {
            return filterVisibleNode(lDatas);
        }

        List<TreeNode> lResult = new ArrayList<TreeNode>();
        String id = lData.getId();
        for (TreeNode bean : lDatas) {
            if (bean.getId().startsWith(id + "_") && bean.isParentExpand()) {//.startWith(id)会导致例如选择短管类（id=2）筛选出法兰（id=20）的错误
                setNodeIcon(bean);
                lResult.add(bean);
            }
        }

        return lResult;
    }

}
