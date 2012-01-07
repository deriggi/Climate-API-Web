/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function(){
    $('#map').mousemove(function(e){
        handleMouseMove(e.pageX,e.pageY);
    })
});

function RadiansToDegrees (rad)
{
    return (rad / Math.PI * 180.0);
}
function DegreesToRadians (deg)
{
    return (deg * Math.PI / 180.0);
}
/* SLVirtualEarthViewer */
// Extent maps to +-180 for Longitude and +-85 for Latitude
function LongLatFromCoordinate (coordinate, worldRect)
{
    var x = ((coordinate.X - worldRect.Left) * 360.0 / worldRect.Width) - 180.0;
    var y = RadiansToDegrees (2 * Math.atan(Math.exp(Math.PI * (1.0 - 2.0 * (coordinate.Y - worldRect.Top) / worldRect.Height))) - Math.PI * 0.5);
    var point = {'X':x, 'Y':y};
    return point;
}
function CoordinateFromLongLat (longLat, worldRect)
{
    var coordinate = {'X':0, 'Y':0};
    coordinate.X = worldRect.Left + (longLat.X + 180.0) * worldRect.Width / 360.0;
    coordinate.Y = worldRect.Top + (worldRect.Width * 0.5) * (1 - (Math.log(Math.tan(DegreesToRadians(longLat.Y) * 0.5 + Math.PI * 0.25))) / Math.PI);
    return coordinate;
}

var world = {'Left':0, 'Top':0, 'Width':720, 'Height':360};

function handleMouseMove(x,y){
    var screenCoord = {'Y':y, 'X':x};
    var ll= LongLatFromCoordinate(screenCoord, world);
    $('#where').text(ll.X + ' ' + ll.Y);

}