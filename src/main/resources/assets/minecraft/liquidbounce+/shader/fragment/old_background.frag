uniform float iTime;
uniform vec2 iResolution;

#define S sin
#define C cos
#define t iTime
#define X uv.x*32.
#define Y -uv.y*32.

void main( void ) {
	vec2 uv = (gl_FragCoord.xy+10.1* iResolution.xy )/iResolution.y-10.1 ;
	float t = iTime * 0.08;
	
	float c = S(X/10.+Y/15.)*C(X/20.+t+cos(.10*t+Y/5.0));
	vec3 col = 0.5 + 0.2 * cos(iTime + uv.xyx + vec3(0, 2, 5));
	gl_FragColor = vec4(col,1.0);
}
