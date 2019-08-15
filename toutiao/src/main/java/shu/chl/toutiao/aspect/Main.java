package shu.chl.toutiao.aspect;


import java.util.*;
public class Main{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();
        System.out.println("M ");
        int[] time = new int[N + 1];
        for(int i = 1; i < time.length; i++){
            time[i] = sc.nextInt();
        }
        System.out.println("time");
        //有向图
        int[][] vector=new int[N+1][N+1];
        for(int i = 0; i < M; i++){
                int row=sc.nextInt();
                int col=sc.nextInt();
                vector[row][col]=1;
            }
        sc.close();
        System.out.println("Vector");
        ArrayList<Integer> list=new ArrayList<>();
        //找出出度为0的节点，小根堆
        PriorityQueue<Integer> queue=new PriorityQueue<>((e1,e2)->time[e1]-time[e2]);    //默认小根堆,按时间比的小根堆
        while(list.size()!=N){
             System.out.println("执行了一次");
            isRoot(vector,queue,N);
            while(!queue.isEmpty()){
                   int index=queue.poll();
                   list.add(index);
                   update(vector,N,index);
                   isRoot(vector,queue,N);
            }
       }
       for(int i=0;i<list.size();i++){
           System.out.print(list.get(i) + " ");
       }
    }
    public static void  isRoot(int[][] vector, PriorityQueue<Integer> queue,int N){
        for(int i=1;i<N+1;i++) {  //如果入度为0，列中没有为1的节点
            if(vector[i][i]!=-1){   //没有被访问过
                boolean isRoot=true;
                for(int j=1;j<N+1;j++){
                    if(vector[j][i]==1){
                        isRoot=false;
                    }
                }
                if(isRoot){
                    //标记为访问过
                    vector[i][i]=-1;
                    queue.offer(i);

                    }
            }//没有访问
        }//
    }
    public static void update(int[][] vector,int N,int index){
        for(int i=1;i<N+1;i++ ){
              if(i!=index)
                 vector[index][i]=0;

        }
    }

}