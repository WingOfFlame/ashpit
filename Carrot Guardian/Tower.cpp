/*
Commands to compile and run:

    g++ -o Tower Tower.cpp -L/usr/X11R6/lib -lX11 -lstdc++
    ./Tower

Note: the -L option and -lstdc++ may not be needed on some machines.

*/


#include <cstdlib>
#include <iostream>
#include <sstream>
#include <string>
#include <list>
#include <unistd.h>
#include <vector>
#include <sys/time.h>
#include <math.h>
#include <stdio.h>
#include "bunny_s.xbm"
#include "splash.xbm"


/*
 * Header files for X functions
 */
#include <X11/Xlib.h>
#include <X11/Xutil.h>

using namespace std;

const int Border = 5;
const int BufferSize = 10;
const int FPS = 30;

//initial size reference
const int o_width = 700;
const int o_height = 300;




/*
 * Information to draw on the window.
 */
struct xinfo {
	Display	 *display;
	int		 screen;
	Window	 window;
	GC		 gc[10];

    Pixmap  pixmap;     // double buffer
    Pixmap  bunny;
    Pixmap  title;
    Pixmap  intro;
    Pixmap  leaf;
    Pixmap  stem;
	int		width;		// size of window
	int		height;
};


/*
 * Function to put out a message on error exits.
 */
void error( string str ) {
    cerr << str << endl;
    exit(0);
}


/*
 * An abstract class representing displayable things. 
 */
class Displayable {
	public:
		virtual void paint(xinfo &xinfo) = 0;
};       


class deadLine :public Displayable{
public:
    virtual void paint(xinfo& xinfo) {
        XDrawLine(xinfo.display, xinfo.pixmap, xinfo.gc[0],
                   x*xinfo.width/o_width,0,x*xinfo.width/o_width,xinfo.height); 
        XDrawLine(xinfo.display, xinfo.pixmap, xinfo.gc[2],
                   x*xinfo.width/o_width,0,x*xinfo.width/o_width,xinfo.height); 
    }
    // constructor
    deadLine(int x):x(x) {}

    int getX() {
        return x;
    }

private:
    int x;

};

class Health :public Displayable{
public:
    virtual void paint(xinfo& xinfo) {
        for(int i = 0;i<x;i++){ 
        if(i>4){
            XCopyPlane(xinfo.display, xinfo.leaf, xinfo.pixmap, xinfo.gc[4], 
            0,0, 
            20, 12,  // region of pixmap to copy
            50*xinfo.width/o_width, (25+55*(i-5))*xinfo.height/o_height,1);
            XCopyPlane(xinfo.display, xinfo.stem, xinfo.pixmap, xinfo.gc[3], 
            0,0, 
            20, 30,  // region of pixmap to copy
            50*xinfo.width/o_width, (25+55*(i-5))*xinfo.height/o_height+12,1);
        } else{      
            XCopyPlane(xinfo.display, xinfo.leaf, xinfo.pixmap, xinfo.gc[4], 
            0,0, 
            20, 12,  // region of pixmap to copy
            25*xinfo.width/o_width, (25+55*i)*xinfo.height/o_height,1);
            XCopyPlane(xinfo.display, xinfo.stem, xinfo.pixmap, xinfo.gc[3], 
            0,0, 
            20, 30,  // region of pixmap to copy
            25*xinfo.width/o_width, (25+55*i)*xinfo.height/o_height+12,1);
            //XFillRectangle(xinfo.display, xinfo.pixmap, xinfo.gc[3],25*xinfo.width/o_width,(15+25*i)*xinfo.height/o_height,20*xinfo.width/o_width,20*xinfo.height/o_height); 
        }

    }
}

    // constructor
    Health(int x):x(x){}

    void losshp(){
        if(x>0){
            x--;
        }
    }

    int gethp(){
        return x;
    }

private:
    int x;

};

class Bunny : public Displayable {
	public:
		virtual void paint(xinfo &xinfo) {

            if(valid == 0){
                XCopyPlane(xinfo.display, xinfo.bunny, xinfo.pixmap, xinfo.gc[1-valid], 
                0,0, 
                40, 40,  // region of pixmap to copy
                x, y,1);
            }else{
                if(type == 2){
                    XCopyPlane(xinfo.display, xinfo.bunny, xinfo.pixmap, xinfo.gc[7], 
                    0,0, 
                    40, 40,  // region of pixmap to copy
                    x, y,1);
                }else if(type == 3){
                    XCopyPlane(xinfo.display, xinfo.bunny, xinfo.pixmap, xinfo.gc[5], 
                    0,0, 
                    40, 40,  // region of pixmap to copy
                    x, y,1);
                }else{
                    XCopyPlane(xinfo.display, xinfo.bunny, xinfo.pixmap, xinfo.gc[1-valid], 
                    0,0, 
                    40, 40,  // region of pixmap to copy
                    x, y,1);
                }
            }

		}
		
		void move(xinfo &xinfo) {
			x = x - direction;
            if(type == 2) x = x - direction/2;
		}
		
		int getX() {
			return x;
		}
		
		int getY() {
			return y;
		}

        int getValid() {
            return valid;
        }
		
		Bunny(int x, int y, int level, int id): x(x), y(y), id(id) {
			direction = 2+level/2;
            type = 1;
            if(rand()%20==1) type = 2;
            if(rand()%20==2) type = 3;
            valid = type;
		}

        void passLine(Health& hp, int score,xinfo &xinfo){
            if(x < 100*xinfo.width/o_width+5){
                hp.losshp();
                valid = 0;
                if(score>0){
                    reset(xinfo);
                }
            }
        }
        //reset bunny to have random type
        void reset(xinfo &xinfo){
            if(valid!= 0){
                valid --;
            }
            if(valid == 0){
                x = xinfo.width+200+20*(rand()%5)+id*150;
                y = xinfo.height/10+ xinfo.height/10*(rand()%8);
                type = 1;
                if(rand()%20==1) type = 2;
                if(rand()%20==2) type = 3;
                valid = type;
            }
        }
        //reposition as window resize
	   void reposition(int ow, int oh, int nw, int nh){
            x = x*nw/ow;
            y = y*nh/oh;
        }
	private:
		int x;
		int y;
		int direction;
        int valid;
        int id;
        int type;
};

class Text : public Displayable {
public:
    virtual void paint(xinfo& xinfo) {
        XDrawImageString( xinfo.display, xinfo.pixmap, xinfo.gc[0],
                          this->x, this->y, this->s.c_str(), this->s.length() );
    }

    // constructor
    Text(int x, int y, string s):x(x), y(y), s(s)  {}

private:
    XPoint p; // a 2D point (see also http://tronche.com/gui/x/xlib/graphics/drawing/)
    int x;
    int y;
    string s; // string to show
};




/*
 * Create a window
 */
void initX(int argc, char* argv[], xinfo& xinfo) {
	XSizeHints hints;
	unsigned long white, black;
    unsigned long colours[8];

    enum {Red=0, Green, Blue, Cyan, Yellow, Magenta, Orange, Brown};

    XColor xcolour;
    Colormap cmap;

   /*
	* Display opening uses the DISPLAY	environment variable.
	* It can go wrong if DISPLAY isn't set, or you don't have permission.
	*/	
	xinfo.display = XOpenDisplay( "" );
	if ( !xinfo.display )	{
		error( "Can't open display." );
	}
	
   /*
	* Find out some things about the display you're using.
	*/
	xinfo.screen = DefaultScreen( xinfo.display );

	white = XWhitePixel( xinfo.display, xinfo.screen );
	black = XBlackPixel( xinfo.display, xinfo.screen );

    char color_vals[8][10]={ "red", "green", "blue", "cyan", "yellow", "magenta", "orange", "brown"};

    cmap=DefaultColormap(xinfo.display,DefaultScreen(xinfo.display));
    for(int i=0; i < 8; ++i) {
        if (!XParseColor(xinfo.display,cmap,color_vals[i],&xcolour)) {
            cerr << "Bad colour: " << color_vals[i] << endl;
        }
        if (!XAllocColor(xinfo.display,cmap,&xcolour)) {
           cerr << "Bad colour: " << color_vals[i] << endl;
        }
        colours[i]=xcolour.pixel;
    }

	hints.x = 100;
	hints.y = 100;
	hints.width = o_width;
	hints.height = o_height;
	hints.min_width = 350;
	hints.min_height = 150;
	hints.max_width = 1400;
	hints.max_height = 600;
	hints.min_aspect.x= 7;
	hints.min_aspect.y= 3;
	hints.flags = PPosition | PSize;

	xinfo.window = XCreateSimpleWindow( 
		xinfo.display,				// display where window appears
		DefaultRootWindow( xinfo.display ), // window's parent in window tree
		hints.x, hints.y,			// upper left corner location
		hints.width, hints.height,	// size of the window
		Border,						// width of window's border
		black,						// window border colour
		white);					// window background colour
		

    // extra window properties like a window title
    XSetStandardProperties(
        xinfo.display,		// display containing the window
        xinfo.window,		// window whose properties are set
        "Tower Defense",	// window's title
        "TD",				// icon's title
        None,				// pixmap for the icon
        argv, argc,			// applications command line args
        &hints );			// size hints for the window


/* 
	 * Create Graphics Contexts
	 */
     //BLACK
	int i = 0;
	xinfo.gc[i] = XCreateGC(xinfo.display, xinfo.window, 0, 0);
	XSetForeground(xinfo.display, xinfo.gc[i], BlackPixel(xinfo.display, xinfo.screen));
	XSetBackground(xinfo.display, xinfo.gc[i], WhitePixel(xinfo.display, xinfo.screen));
	XSetFillStyle(xinfo.display, xinfo.gc[i], FillSolid);
	XSetLineAttributes(xinfo.display, xinfo.gc[i],
	                     1, LineSolid, CapButt, JoinRound);

	// WHITE
	i = 1;
	xinfo.gc[i] = XCreateGC(xinfo.display, xinfo.window, 0, 0);
	XSetForeground(xinfo.display, xinfo.gc[i], WhitePixel(xinfo.display, xinfo.screen));
	XSetBackground(xinfo.display, xinfo.gc[i], BlackPixel(xinfo.display, xinfo.screen));
	XSetFillStyle(xinfo.display, xinfo.gc[i], FillSolid);
	XSetLineAttributes(xinfo.display, xinfo.gc[i],
	                     1, LineSolid, CapButt, JoinRound);
    
    //BLACK dash line
    i = 2;
    xinfo.gc[i] = XCreateGC(xinfo.display, xinfo.window, 0, 0);
    XSetForeground(xinfo.display, xinfo.gc[i], BlackPixel(xinfo.display, xinfo.screen));
    XSetBackground(xinfo.display, xinfo.gc[i], WhitePixel(xinfo.display, xinfo.screen));
    XSetFillStyle(xinfo.display, xinfo.gc[i], FillSolid);
    XSetLineAttributes(xinfo.display, xinfo.gc[i],
                         15, LineOnOffDash, CapButt, JoinBevel);

    //orange
    i = 3;
    xinfo.gc[i] = XCreateGC(xinfo.display, xinfo.window, 0, 0);
    XSetForeground(xinfo.display, xinfo.gc[i], colours[Orange]);
    XSetBackground(xinfo.display, xinfo.gc[i], WhitePixel(xinfo.display, xinfo.screen));


    //green
    i = 4;
    xinfo.gc[i] = XCreateGC(xinfo.display, xinfo.window, 0, 0);
    XSetForeground(xinfo.display, xinfo.gc[i], colours[Green]);
    XSetBackground(xinfo.display, xinfo.gc[i], WhitePixel(xinfo.display, xinfo.screen));

    //red
    i = 5;
    xinfo.gc[i] = XCreateGC(xinfo.display, xinfo.window, 0, 0);
    XSetForeground(xinfo.display, xinfo.gc[i], colours[Red]);
    XSetBackground(xinfo.display, xinfo.gc[i], WhitePixel(xinfo.display, xinfo.screen));


    //yellow
    i = 6;
    xinfo.gc[i] = XCreateGC(xinfo.display, xinfo.window, 0, 0);
    XSetForeground(xinfo.display, xinfo.gc[i], colours[Yellow]);
    XSetBackground(xinfo.display, xinfo.gc[i], WhitePixel(xinfo.display, xinfo.screen));

    //blue
    i = 7;
    xinfo.gc[i] = XCreateGC(xinfo.display, xinfo.window, 0, 0);
    XSetForeground(xinfo.display, xinfo.gc[i], colours[Blue]);
    XSetBackground(xinfo.display, xinfo.gc[i], WhitePixel(xinfo.display, xinfo.screen));


    //brown
    i = 8;
    xinfo.gc[i] = XCreateGC(xinfo.display, xinfo.window, 0, 0);
    XSetForeground(xinfo.display, xinfo.gc[i], colours[Brown]);
    XSetBackground(xinfo.display, xinfo.gc[i], WhitePixel(xinfo.display, xinfo.screen));


    int depth = DefaultDepth(xinfo.display, DefaultScreen(xinfo.display));
    xinfo.pixmap = XCreatePixmap(xinfo.display, xinfo.window, hints.width, hints.height, depth);
    xinfo.width = hints.width;
    xinfo.height = hints.height;


    //creating a image as pixmap

    /* these variables will contain the dimensions of the loaded bitmap. */
    unsigned int bit_width, bit_height;
    /* these variables will contain the location of the hotspot of the   */
    /* loaded bitmap.                                                    */
    int hotspot_x, hotspot_y;

    int rc = XReadBitmapFile(xinfo.display, xinfo.window,
                             "bunny_s.xbm",
                             &bit_width, &bit_height,
                             &xinfo.bunny,
                             &hotspot_x, &hotspot_y);

    XReadBitmapFile(xinfo.display, xinfo.window,
                             "title.xbm",
                             &bit_width, &bit_height,
                             &xinfo.title,
                             &hotspot_x, &hotspot_y);

    XReadBitmapFile(xinfo.display, xinfo.window,
                             "intro.xbm",
                             &bit_width, &bit_height,
                             &xinfo.intro,
                             &hotspot_x, &hotspot_y);

    XReadBitmapFile(xinfo.display, xinfo.window,
                             "bunny_s.xbm",
                             &bit_width, &bit_height,
                             &xinfo.bunny,
                             &hotspot_x, &hotspot_y);

    XReadBitmapFile(xinfo.display, xinfo.window,
                             "leaf.xbm",
                             &bit_width, &bit_height,
                             &xinfo.leaf,
                             &hotspot_x, &hotspot_y);

    XReadBitmapFile(xinfo.display, xinfo.window,
                             "stem.xbm",
                             &bit_width, &bit_height,
                             &xinfo.stem,
                             &hotspot_x, &hotspot_y);

     /* check for failure or success. */
    switch (rc) {
        case BitmapOpenFailed:
            fprintf(stderr, "XReadBitmapFile - could not open file 'bunny_s.xbm'.\n");
	    exit(1);
            break;
        case BitmapFileInvalid:
            fprintf(stderr,
                    "XReadBitmapFile - file '%s' doesn't contain a valid bitmap.\n",
                    "bunny_s.xbm");
	    exit(1);
            break;
        case BitmapNoMemory:
            fprintf(stderr, "XReadBitmapFile - not enough memory.\n");
	    exit(1);
            break;
    }  


	XSelectInput(xinfo.display, xinfo.window,
        ButtonPressMask | KeyPressMask |
        ExposureMask | ButtonMotionMask|
		StructureNotifyMask| EnterWindowMask | LeaveWindowMask);  // for resize events
    /*
     * Don't paint the background -- reduce flickering
     */
    XSetWindowBackgroundPixmap(xinfo.display, xinfo.window, None);

 /*
     * Put the window on the screen.
     */
    XMapRaised( xinfo.display, xinfo.window );

    Atom WM_DELETE_WINDOW = XInternAtom(xinfo.display, "WM_DELETE_WINDOW", True); 
    XSetWMProtocols(xinfo.display, xinfo.window, &WM_DELETE_WINDOW, 1);

    XFlush(xinfo.display);
    sleep(2);	// let server get set up before sending drawing commands
}


void clearWindow(xinfo &xinfo){
    XClearWindow( xinfo.display, xinfo.window );
    //white background        
    XFillRectangle(xinfo.display, xinfo.pixmap, xinfo.gc[1], 
    0, 0, xinfo.width, xinfo.height);
}

/*
 * Function to repaint a display list
 */
void repaint( list<Displayable*> dList, xinfo& xinfo) {
    list<Displayable*>::const_iterator begin = dList.begin();
    list<Displayable*>::const_iterator end = dList.end();

    while( begin != end ) {
        Displayable* d = *begin;
        d->paint(xinfo);
        begin++;
    }

}


void repaint( list<Bunny*> dList, xinfo& xinfo) {
    list<Bunny*>::const_iterator begin = dList.begin();
    list<Bunny*>::const_iterator end = dList.end();

    while( begin != end ) {
        Bunny* b = *begin;
        b->paint(xinfo);
        begin++;
    }

}

void reposition( list<Bunny*> dList, int ow, int oh, xinfo& xinfo) {
    list<Bunny*>::const_iterator begin = dList.begin();
    list<Bunny*>::const_iterator end = dList.end();

    while( begin != end ) {
        Bunny* b = *begin;
        b->reposition(ow,oh,xinfo.width,xinfo.height);
        begin++;
    }

}



int clickCheck(int x, int y, list<Bunny*> dList,xinfo& xinfo){
    list<Bunny*>::const_iterator begin = dList.begin();
    list<Bunny*>::const_iterator end = dList.end();

    while( begin != end ) {
        Bunny* d = *begin;
        int bx = d->getX();
        int by = d->getY();
        if(abs(y-(by+20)) < 25&&
            abs(x-(bx+20)) < 25)
        {
            d->reset(xinfo);
            return 1;
        }
        begin++;
    }
    return 0;
}

// update width and height when window is resized
void handleResize(xinfo &xinfo, XEvent &event) {
    XConfigureEvent xce = event.xconfigure;
    XFreePixmap(xinfo.display, xinfo.pixmap);
    int depth = DefaultDepth(xinfo.display, DefaultScreen(xinfo.display));
    xinfo.pixmap = XCreatePixmap(xinfo.display, xinfo.window, xce.width, xce.height, depth);
    xinfo.width = xce.width;
    xinfo.height = xce.height;
}


void handleAnimation(list<Bunny*> dList, xinfo& xinfo, Health& hp, int score) {
    list<Bunny*>::const_iterator begin = dList.begin();
    list<Bunny*>::const_iterator end = dList.end();
    while( begin != end ) {
        Bunny* d = *begin;
        d->move(xinfo);
        d->passLine(hp,score,xinfo);
        begin++;
    }
}


void handleKeyPress(xinfo &xinfo, XEvent &event) {
    KeySym key;
    char text[BufferSize];
    
    /*
     * Exit when 'q' is typed.
     * This is a simplified approach that does NOT use localization.
     */
    int i = XLookupString( 
        (XKeyEvent *)&event,    // the keyboard event
        text,                   // buffer when text will be written
        BufferSize,             // size of the text buffer
        &key,                   // workstation-independent key symbol
        NULL );                 // pointer to a composeStatus structure (unused)
    if ( i == 1) {
        printf("Got key press -- %c\n", text[0]);
        if (text[0] == 'q') {
            error("Terminating normally.");
        }
    }
}


unsigned long now() {
    timeval tv;
    gettimeofday(&tv, NULL);
    return tv.tv_sec * 1000000 + tv.tv_usec;
}


/*
 * The loop responding to events from the user.
 */
int eventloop(xinfo& xinfo, int level) {
    XEvent event;
    string s;
	unsigned long lastRepaint = 0;
	int inside = 0;

    list<Displayable*> dList;            // list of Displayables
    list<Bunny*> bunnies;               //list of bunnies

    deadLine baseline(100);
    Health hp(10);

    dList.push_back(&hp);
    dList.push_back(&baseline);

    int score = level * 5 +10;

    for(int i = 0; i < 10; i++ ){
        bunnies.push_back(new Bunny(xinfo.width+i*100+50*(rand()%2), xinfo.height/10+xinfo.height/10*(rand()%8),level,i));
    }

    while( score > 0 ) {

        if(xinfo.width<=699 || xinfo.height<=299){

            usleep(1000000/FPS);
            XFillRectangle(xinfo.display, xinfo.window, xinfo.gc[1], 0, 0, xinfo.width, xinfo.height);
            if(xinfo.width>700){
                s = "Window too short";
            }else if(xinfo.height>300){
                s = "Window too narrow";
            }else{
                s = "Window too small";
            }
            XDrawImageString( xinfo.display, xinfo.window, xinfo.gc[0], xinfo.width/3, xinfo.height/3, s.c_str(), s.length() );
            XFlush( xinfo.display );

        }else if(XPending(xinfo.display) == 0) {

            usleep(1000000/FPS);
            handleAnimation(bunnies,xinfo, hp, score);
            // draw into the buffer
            // note that a window and a pixmap are “drawables”
            XFillRectangle(xinfo.display, xinfo.pixmap, xinfo.gc[1], 0, 0, xinfo.width, xinfo.height);
            repaint(dList,xinfo );
            repaint(bunnies,xinfo);
            // copy buffer to window
            XCopyArea(xinfo.display, xinfo.pixmap, xinfo.window, xinfo.gc[1], 
                0, 0, xinfo.width, xinfo.height,  // region of pixmap to copy
                0, 0); // position to put top left corner of pixmap in window
        }

        XNextEvent( xinfo.display, &event );

        int ow = xinfo.width;
        int oh = xinfo.height;

        switch( event.type ) {
            case ConfigureNotify:
                handleResize(xinfo, event);
                reposition(bunnies,ow,oh,xinfo);
                break;  
            case ButtonPress:
                score -= clickCheck(event.xbutton.x, event.xbutton.y, bunnies,xinfo);
                break;
            case KeyPress:
                handleKeyPress(xinfo, event);
                break;
            case ClientMessage:
                error("Terminating normally.");
                break;
        }
        if(hp.gethp()<1){
            return 0;
        }
    }
    return 1;
}

void splashScreen(xinfo &xinfo){
    XEvent event;
    clearWindow(xinfo);
    //title
    XCopyPlane(xinfo.display, xinfo.title, xinfo.pixmap, xinfo.gc[3], 
    0,0, 
    385, 50,  // region of pixmap to copy
    165, 25,1);
    //intro text
    XCopyPlane(xinfo.display, xinfo.intro, xinfo.pixmap, xinfo.gc[0], 
    0,0, 
    383, 128,  // region of pixmap to copy
    165, 90,1);
    //put on display
    XCopyArea(xinfo.display, xinfo.pixmap, xinfo.window, xinfo.gc[1], 
    0, 0, xinfo.width, xinfo.height,  // region of pixmap to copy
    0, 0); // position to put top left corner of pixmap in window

    string s = "Click any where to begin. Press Q to quit.";
    XDrawImageString( xinfo.display, xinfo.window, xinfo.gc[0], 215, 250, s.c_str(), s.length() );
    XFlush( xinfo.display );

    int cont = 0;
    while(!cont){
        XNextEvent( xinfo.display, &event );
        switch( event.type ) {
            case ClientMessage:
                error("Terminating normally.");
                break;
            case ConfigureNotify:
                handleResize(xinfo, event);
                break;  
            case ButtonPress:
                cont = 1;
                break;
            case KeyPress:
                handleKeyPress(xinfo, event);
                break;
            // Repaint the window on expose events.
            case Expose:
                if ( event.xexpose.count == 0 ) {
                    clearWindow(xinfo);
                    XCopyPlane(xinfo.display, xinfo.title, xinfo.pixmap, xinfo.gc[3], 
                    0,0, 
                    385, 50,  // region of pixmap to copy
                    165, 25,1);
                    //intro text
                    XCopyPlane(xinfo.display, xinfo.intro, xinfo.pixmap, xinfo.gc[0], 
                    0,0, 
                    383, 128,  // region of pixmap to copy
                    165, 90,1);
                    //put on display
                    XCopyArea(xinfo.display, xinfo.pixmap, xinfo.window, xinfo.gc[1], 
                    0, 0, xinfo.width, xinfo.height,  // region of pixmap to copy
                    0, 0); // position to put top left corner of pixmap in window
                    XDrawImageString( xinfo.display, xinfo.window, xinfo.gc[0], 215, 250, s.c_str(), s.length() );
                    XFlush( xinfo.display );
                }
                break;
        }
    }
}




/*
 *   Start executing here.
 *	 First initialize window.
 *	 Next loop responding to events.
 *	 Exit forcing window manager to clean up - cheesy, but easy.
 */
int main ( int argc, char* argv[] ) {
    srand(time(0));
    
    int level = 1;
    XEvent event;
    xinfo xinfo;
    string s;


    initX(argc, argv, xinfo);
    splashScreen(xinfo);
    

    //loop for infinite level
    //each level has its own event loop        
    while(true){
        if(eventloop(xinfo,level)==0){
            ostringstream convert;   // stream used for the conversion
            convert << level;      // insert the textual representation of 'Number' in the characters in the stream
            s = "You failed at level "+ convert.str()+". Click any where to restart, or press Q to quit.";
            level = 1;
        }else{
            level ++;
            s = "LEVEL UP!  Click any where to continue, or press Q to quit.";
        }
        clearWindow(xinfo);
        XFillRectangle(xinfo.display, xinfo.window, xinfo.gc[1], 0, 0, xinfo.width, xinfo.height);
        XDrawImageString( xinfo.display, xinfo.window, xinfo.gc[0],xinfo.width/4, xinfo.height/2, s.c_str(), s.length() );
        XFlush( xinfo.display );
        usleep(2000000);

        //wait for keypress to continue 
        int cont = 0;
        while(!cont){
            XNextEvent( xinfo.display, &event );
            switch( event.type ) {
                case ConfigureNotify:
                    handleResize(xinfo, event);
                    break;  
                    // add item where mouse clicked
                case ButtonPress:
                    cont = 1;
                    break;
                case KeyPress:
                    handleKeyPress(xinfo, event);
                    break;
                // Repaint the window on expose events.
                case Expose:
                if ( event.xexpose.count == 0 ) {
                    XFillRectangle(xinfo.display, xinfo.window, xinfo.gc[1], 0, 0, xinfo.width, xinfo.height);
                    XDrawImageString( xinfo.display, xinfo.window, xinfo.gc[0],xinfo.width/4, xinfo.height/2, s.c_str(), s.length() );
                    XFlush( xinfo.display );
                }
                    break;
                case ClientMessage:
                    error("Terminating normally.");
                    break;
            }
        }
    }
    XCloseDisplay(xinfo.display);
}

