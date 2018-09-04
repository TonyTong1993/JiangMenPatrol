package com.ecity.android.map.core.local;

import com.ecity.android.map.core.dbquery.ECityRect;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.identify.IdentifyParameters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Administrator 管线数据查询器，封装所有管段数据库查询
 */
public class ECityPipeDbQueryer {

    private static ECityPipeDbQueryer instance;

    /**
     * 判断数据库是否存在
     */
    private boolean isSQLiteDatabaseExisted = false;

    public static ECityPipeDbQueryer getInstance() {
        if (instance == null) {
            instance = new ECityPipeDbQueryer();
            instance.getDatabaseExisted();
        }
        return instance;
    }

    private ECityPipeDbQueryer() {

    }

    /**
     * 获取数据库是否存在
     */
    private void getDatabaseExisted() {
        isSQLiteDatabaseExisted = PipeDB.getInstance()
                .isSQLiteDatabaseExisted();
        System.out.println("database existed called");
    }

    public void changeDB(String dbName) {
        isSQLiteDatabaseExisted = false;
        instance = null;
        System.out.println("pipechange:" + dbName);
        PipeDB.getInstance().changeDB(dbName);
    }

    public List<Graphic> queryByParam(DbQueryParam param) {
        if (param == null) {
            return null;
        }
        List<Graphic> graphics = new ArrayList<Graphic>();

        if (param.layerMode == IdentifyParameters.ALL_LAYERS) {
            // 查询所有的表
            graphics = queryAllGraphics(param.whereClause, param.rect);
        } else {
            List<String> layerNames = param.layerName;

            List<Integer> dnos = param.dnos;

            for (Iterator iterator = layerNames.iterator(); iterator.hasNext();) {
                String name = (String) iterator.next();
                List<Graphic> nodes = queryNode(name, dnos, param.whereClause,
                        param.rect);
                List<Graphic> lines = queryLine(name, dnos, param.whereClause,
                        param.rect);
                graphics.addAll(nodes);
                graphics.addAll(lines);
            }

        }

        return graphics;

    }

    private List<Graphic> queryAllGraphics(String where, ECityRect rct) {
        return PipeDB.getInstance().getAllGraphicsFromWhere(where, rct);
    }

    private List<Graphic> queryNode(String layerName, List<Integer> dnos,
            String where, ECityRect rect) {
        return PipeDB.getInstance().queryNode(layerName, dnos, where, rect);
    }

    private List<Graphic> queryLine(String layerName, List<Integer> dnos,
            String where, ECityRect rect) {
        return PipeDB.getInstance().queryLine(layerName, dnos, where, rect);
    }
    // /**
    // * 从矩形区取出所有Id列表
    // * @param rect
    // * @return
    // */
    // public List<ECityDataId> getIdsByRect( ECityRect rect) {
    //
    // if (!isSQLiteDatabaseExisted) {
    // return null;
    // }
    // if (rect != null) {
    // List<ECityDataId> list = new ArrayList<ECityDataId>();
    // List<Integer> pipes = PipeDB.getInstance().getLineListByRect(
    // rect.left, rect.right, rect.top, rect.bottom);
    // List<Integer> nodes = PipeDB.getInstance().getNodeListByRect(
    // rect.left, rect.right, rect.top, rect.bottom);
    // if (pipes != null) {
    // for (Iterator<Integer> iterator = pipes.iterator(); iterator.hasNext();)
    // {
    // Integer integer = (Integer) iterator.next();
    // list.add(new ECityDataId(ECityDataId.type_pipe, integer));
    // }
    // }
    //
    // if (nodes != null) {
    // for (Iterator<Integer> iterator = nodes.iterator(); iterator.hasNext();)
    // {
    // Integer integer = (Integer) iterator.next();
    // list.add(new ECityDataId(ECityDataId.type_node, integer));
    // }
    // }
    //
    // return list;
    // }
    //
    // return null;
    // }

    //
    // /**
    // * 查询Ids相关的数据
    // * @param ids
    // * @param allGraphics 返回的所有graphics列表
    // * @param need_nodes 需要建模的node
    // * @param need_pipes 需要建模的pipe
    // * @return
    // */
    // public List<ECityGraphic> queryPipeDataByList( List<ECityDataId> ids ) {
    // if (ids == null || ids.size() == 0 ) {
    // return null;
    // }
    //
    // //想构建一个pipe，必须要查到这个pipe两边的node，
    // //想构建一个node，必须查到和这个node相关的所有信息
    //
    //
    // List<ECityGraphic> allGraphics = new ArrayList<ECityGraphic>();
    //
    // //组建pipe和node的列表
    // Set<Integer> nodeIds = new HashSet<Integer>();
    // Set<Integer> pipeIds = new HashSet<Integer>();
    //
    // for (Iterator<ECityDataId> iterator = ids.iterator();
    // iterator.hasNext();) {
    // ECityDataId id = (ECityDataId) iterator.next();
    // if (id.type == ECityDataId.type_pipe) {
    // pipeIds.add(id.id);
    // }
    // else if (id.type == ECityDataId.type_node) {
    // nodeIds.add(id.id);
    // }
    // }
    //
    // //查pipe
    // List<ECityGraphic> pipes =
    // PipeDB.getInstance().getPipeGraphicsByPipeID(pipeIds);
    //
    // if (pipes != null) {
    // Set<Integer> addNodes = new HashSet<Integer>();
    // for (Iterator<ECityGraphic> iterator = pipes.iterator();
    // iterator.hasNext();) {
    // ECityGraphic graphic = (ECityGraphic) iterator.next();
    //
    // if (graphic.getGraphicType() == ECityDataId.type_pipe) {
    //
    // Map<String, Object> attMap = graphic.getAttributes();
    //
    // int start_node = attMap.get("stnod") == null ? 0
    // : (Integer) attMap.get("stnod");
    // int end_node = attMap.get("ednod") == null ? 0
    // : (Integer) attMap.get("ednod");
    // if (!nodeIds.contains(start_node)) {
    // addNodes.add(start_node);
    // }
    // if (!nodeIds.contains(end_node)) {
    // addNodes.add(end_node);
    // }
    // }
    //
    // }
    //
    //
    // //查node
    // nodeIds.addAll(addNodes);
    // allGraphics.addAll(pipes);
    // }
    //
    // List<ECityGraphic> nodes =
    // PipeDB.getInstance().getNodeGraphicsByNodeId(nodeIds);
    //
    //
    // if (nodes != null) {
    // allGraphics.addAll(nodes);
    // }
    //
    //
    //
    // return allGraphics;
    // }

    // /**
    // * 根据范围查询数据
    // *
    // * @param rectF
    // * @return
    // */
    // private boolean queryPipeDataByRect(RectF rectF) {
    // if (rectF == null) {
    // return false;
    // }
    //
    // List<Graphic> allGraphics = null;
    // List<Graphic> inRectPipes = new ArrayList<Graphic>();
    // try {
    // allGraphics = DBWorkSpace.getInstance().getGraphicsByRectF(rectF,
    // inRectPipes);
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // classifyGraphics(allGraphics, inRectPipes);
    //
    // return true;
    // }

}
