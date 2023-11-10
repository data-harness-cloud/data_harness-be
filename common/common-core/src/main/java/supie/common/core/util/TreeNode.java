package supie.common.core.util;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 将列表结构组建为树结构的工具类。
 *
 * @param <T> 对象类型。
 * @param <K> 节点之间关联键的类型。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Data
public class TreeNode<T, K> {

	private K id;
	private K parentId;
	private T data;
	private List<TreeNode<T, K>> childList = new ArrayList<>();

    /**
     * 将列表结构组建为树结构的工具方法。
     *
     * @param dataList     数据列表结构。
     * @param idFunc       获取关联id的函数对象。
     * @param parentIdFunc 获取关联ParentId的函数对象。
     * @param root         根节点。
     * @param <T>          数据对象类型。
     * @param <K>          节点之间关联键的类型。
     * @return 源数据对象的树结构存储。
     */
    public static <T, K> List<TreeNode<T, K>> build(
            List<T> dataList, Function<T, K> idFunc, Function<T, K> parentIdFunc, K root) {
        List<TreeNode<T, K>> treeNodeList = new ArrayList<>();
        for (T data : dataList) {
            if (ObjectUtil.equals(parentIdFunc.apply(data), idFunc.apply(data))) {
                continue;
            }
            TreeNode<T, K> dataNode = new TreeNode<>();
            dataNode.setId(idFunc.apply(data));
            dataNode.setParentId(parentIdFunc.apply(data));
            dataNode.setData(data);
            treeNodeList.add(dataNode);
        }
        return root == null ? toBuildTreeWithoutRoot(treeNodeList) : toBuildTree(treeNodeList, root);
    }

    private static <T, K> List<TreeNode<T, K>> toBuildTreeWithoutRoot(List<TreeNode<T, K>> treeNodes) {
        Map<K, TreeNode<T, K>> treeNodeMap =
                treeNodes.stream().collect(Collectors.toMap(TreeNode::getId, n -> n));
        List<TreeNode<T, K>> treeNodeList = new ArrayList<>();
        for (TreeNode<T, K> treeNode : treeNodes) {
            TreeNode<T, K> parentNode = treeNodeMap.get(treeNode.getParentId());
            if (parentNode == null) {
                treeNodeList.add(treeNode);
            } else {
                parentNode.add(treeNode);
            }
        }
        return treeNodeList;
    }

    private static <T, K> List<TreeNode<T, K>> toBuildTree(List<TreeNode<T, K>> treeNodes, K root) {
        List<TreeNode<T, K>> treeNodeList = new ArrayList<>();
		for (TreeNode<T, K> treeNode : treeNodes) {
			if (root.equals(treeNode.getParentId())) {
				treeNodeList.add(treeNode);
			}
			for (TreeNode<T, K> it : treeNodes) {
				if (it.getParentId() == treeNode.getId()) {
					if (treeNode.getChildList() == null) {
						treeNode.setChildList(new ArrayList<>());
					}
					treeNode.add(it);
				}
			}
		}
		return treeNodeList;
	}

    private void add(TreeNode<T, K> node) {
        childList.add(node);
    }
}
