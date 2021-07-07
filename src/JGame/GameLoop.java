/*
 * http://entropyinteractive.com/tutorials/
 */
package JGame;

public abstract class GameLoop {
    private boolean runFlag = false;
    protected int FPS; //Mati

    /**
     * Begin the game loop
     * @param delta time between logic updates (in seconds)
     */
    public void run(double delta) {
        runFlag = true;

        startup();
        // convert the time to seconds
        double nextTime = (double) System.nanoTime() / 1000000000.0;
        double maxTimeDiff = 0.5;
        int skippedFrames = 1;
        int maxSkippedFrames = 5;
        
        long now=0;//Almacena el tiempo que se registro ahora en el hilo actual
        long lastTime=System.nanoTime();//almacena el tiempo que se registro en el ultimo hilo
        int promFPS=0;//Almacena la cantidad de fotogramas realizados 
        long time=0;/*Almacena el tiempo trascurrido en nanosegundos, hasta que la misma sea igual a un millon de 
        nanosegundos, osea 1 segundo, momento en el cual se procedera a mostrar la cantidad de frames realizados mediante 
        la variable frames.*/
        
        while (runFlag) {
            now=System.nanoTime();//MATY
            time+=(now-lastTime);//MATY
            lastTime=now;       //MATY
            
            // convert the time to seconds
            double currTime = (double) System.nanoTime() / 1000000000.0;
            if ((currTime - nextTime) > maxTimeDiff) nextTime = currTime;
            if (currTime >= nextTime) {
                // assign the time for the next update
                nextTime += delta;

                /**********************************************************************/
                update(delta);
                /**********************************************************************/

                if ((currTime < nextTime) || (skippedFrames > maxSkippedFrames)) {

                	/**********************************************************************/
                    draw();
                    /**********************************************************************/
                    skippedFrames = 1;
                    promFPS++;//MATY
                } else {
                    skippedFrames++;
                }
                
                if(time >= 1000000000){ //Cuando time sea igual a 1 segundo se mustra la cantidad de Frames realizados
                FPS=promFPS;
                promFPS=0;
                time=0;
                }
                
            } else {
                // calculate the time to sleep
                int sleepTime = (int)(1000.0 * (nextTime - currTime));
                // sanity check
                if (sleepTime > 0) {
                    // sleep until the next update
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        // do nothing
                    }
                }
            }
        }
        shutdown();
    }

    public void stop() {
        runFlag = false;
    }

    public abstract void startup();
    public abstract void shutdown();
    protected abstract void update(double delta);
    public abstract void draw();
}