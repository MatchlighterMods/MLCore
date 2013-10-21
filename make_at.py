import re
import os, os.path
import sys

mcp_home = sys.argv[1]
mcp_dir = os.path.abspath(mcp_home)
cfg_dir = os.path.join(mcp_dir, 'conf')

joined_srg = os.path.join(cfg_dir, 'joined.srg')

for f in os.listdir('./'):
	n = re.match(r'(.*_at)\.man', f)
	if n:
		with open(n.group(1)+'.cfg', 'w+') as oat:
			for instr in open(f, 'r+'):
				instr = re.sub(r'\s*#.*', '', instr)
				instr=instr.strip()
				nat = re.match(r'(.*) (.*)$', instr)
				if nat:
					with open(joined_srg, 'r+') as jnd:
						for ln in jnd:
							if ln.find(nat.group(2))>-1:
								at = re.match(r'(..): (\w+/\w+) ?(.*) net/.*', ln)
								oat.write('%s %s%s'%(nat.group(1), at.group(2).replace('/', '.'), at.group(3)))