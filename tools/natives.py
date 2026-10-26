import re, os, sys, json, collections
ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'src', 'main', 'java')
PAT = re.compile(r'(?P<mods>(?:(?:private|public|protected|static|final|synchronized)\s+)*)native\s+(?P<ret>[\w\[\]\.]+)\s+(?P<name>\w+)\s*\((?P<params>[^)]*)\)\s*;', re.S)
def scan():
    out = []
    for d, _, fs in os.walk(ROOT):
        for f in fs:
            if not f.endswith('.java'): continue
            p = os.path.join(d, f)
            src = open(p, encoding='utf-8').read()
            for m in PAT.finditer(src):
                params = [x.strip() for x in re.sub(r'\s+', ' ', m.group('params')).split(',') if x.strip()]
                ptypes = [' '.join(x.split()[:-1]) for x in params]
                pnames = [x.split()[-1] for x in params]
                out.append(dict(file=os.path.relpath(p, ROOT), mods=m.group('mods').split(), ret=m.group('ret'), name=m.group('name'), ptypes=ptypes, pnames=pnames, start=m.start(), end=m.end()))
    return out
if __name__ == '__main__':
    ns = scan()
    print(len(ns))
    by = collections.defaultdict(set)
    for n in ns:
        if 'static' in n['mods']:
            by[(n['name'], tuple(n['ptypes']))].add(n['ret'])
    print('unique static sigs', len(by), 'names', len({k[0] for k in by}))
    for k, v in by.items():
        if len(v) > 1: print('RETCONFLICT', k, v)
    print('non-static:')
    for n in ns:
        if 'static' not in n['mods']: print(' ', n['file'], n['name'])
    json.dump(ns, open('natives.json', 'w'), indent=0)
