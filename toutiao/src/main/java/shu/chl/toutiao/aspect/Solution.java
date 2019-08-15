package shu.chl.toutiao.aspect;


public class Solution {
    public static int index=0;
    public class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;

        }


    }
    StringBuilder str=new StringBuilder();

    String Serialize(TreeNode root) {
        if(root==null)
            return null;
        //先序遍历
        str.append(root.val+",");
        if(root.left==null)
            str.append("#,");
        else
            Serialize(root.left);
        if(root.right==null)
            str.append("#,");
        else
            Serialize(root.right);
        return str.toString();

    }
    TreeNode Deserialize(String str) {
        if(str==null||str.length()<1)
            return null;
        String[] array=str.split(",");
        TreeNode root=Constroct(array,Solution.index);
        return root;
    }
    TreeNode Constroct(String[] array,int index){
        if(index==array.length-1)
            return null;
        if(!array[index].equals("#")){
            TreeNode root=new TreeNode(Integer.parseInt(array[index]));
            root.left=Constroct(array,++Solution.index);
            root.right=Constroct(array,++Solution.index);
            return root;
        }else{
            return null;
        }

    }
    public static void main(String[] args){
        Solution solution=new Solution();
        String str="8,6,5,#,#,7,#,#,10,9,#,#,11,#,#,";
        TreeNode root=solution.Deserialize(str);
        System.out.println(root);
        String str1=solution.Serialize(root);
        System.out.println(str1);
    }
}