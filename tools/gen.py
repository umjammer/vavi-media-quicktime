import re, os, json, collections
from natives import scan, ROOT
PKG = 'quicktime.internal.toolbox'
OUT = os.path.join(ROOT, PKG.replace('.', '/'))

RULES = [
 ('MemoryManager', r'^(get|set)(Byte|Short|Int|Long|Float|Double)(From|In)(Array|Handle|Pointer)$|^copy(Array|Pointer)To|^(NewHandle|NewHandleClear|NewEmptyHandle|DisposeHandle|GetHandleSize|SetHandleSize|HLock|HLockHi|HUnlock|MoveHHi|NewPtr|NewPtrClear|DisposePtr|SetPtrSize|BlockMove|MemError|ReserveMem|PtrToHand|HandAndHand)$'),
 ('PlatformManager0', None),
 ('MovieToolbox', r'^(?!MC|Media|FlashMedia|TextMedia|VideoMedia|MovieMedia|MusicMedia|MovieExport|MovieImport|GraphicsImport|GraphicsExport|SG|CDSequence|QTS|QTVR|Sprite)[A-Za-z]*(Movie|Track|Media|UserData)(?!Controller$)'),
 ('MatrixManager', r'Matrix$|^GetMatrixType$|^(TransformPoints|TransformFixedPoints|TransformRect|TransformFixedRect|TransformRgn)$|^QuadToQuadMatrix$'),
 ('QD3DManager', r'^Q3'),
 ('VRManager', r'^QTVR|^(InitializeQTVR|TerminateQTVR)$'),
 ('StreamingManager', r'^QTS'),
 ('MusicManager', r'^(NA|Tune|Music)'),
 ('SoundManager', r'^(Snd|SPB)|^(SetupAIFFHeader|SetupSndHeader|ParseAIFFHeader|ParseSndHeader|GetSoundHeaderOffset|GetSoundDescriptionExtension|AddSoundDescriptionExtension|RemoveSoundDescriptionExtension)$'),
 ('SequenceGrabberManager', r'^(SG|VD)'),
 ('MovieControllerManager', r'^MC|^(NewMovieController|DisposeMovieController)$'),
 ('ClockManager', r'TimeBase|CallBack|^(ClockGetTime|AddTime|SubtractTime|ConvertTime|ConvertTimeScale|CallMeWhen|QTGetWallClockTimeBase|TickCount)$'),
 ('AtomManager', r'^QT(New|Dispose)AtomContainer$|Atom(s|Parent|TypeAndID|ID|DataPtr|DataToHandle|DataToPtr|Data)?$|^QT(Lock|Unlock)Container$|^QT(InsertChild|InsertChildren|RemoveChildren|FindChildByID|FindChildByIndex|CountChildrenOfType|GetNextChildType|NextChildAnyType)$'),
 ('FileManager', r'^(FS|PB)|Alias|^(QTNewDataReferenceFromFullPathUnicode|StandardGetFilePreview)$'),
 ('ImageCompressionManager', r'^(CDSequence|GraphicsImport|GraphicsExport|GetGraphicsImporter|SC|ICM|Curve|DataCodec)|Sequence|Codec|^(CompressImage|FCompressImage|CompressPicture|FCompressPicture|DecompressImage|FDecompressImage|ConvertImage|TrimImage|GetMaxCompressionSize|GetCompressedImageSize|GetCompressionInfo|GetCompressionTime|GetSimilarity|MakeThumbnailFromPicture|MakeThumbnailFromPixMap|MakeImageDescriptionForPixMap|MakeImageDescriptionForEffect|QTGetPixelSize|GetImageDescriptionCTable|SetImageDescriptionCTable|GetImageDescriptionExtension|AddImageDescriptionExtension|RemoveImageDescriptionExtension|CountImageDescriptionExtensionType|GetNextImageDescriptionExtensionType|HitTestDSequenceData|PtInDSequenceData)$|DSequence'),
 ('SpriteManager', r'Sprite'),
 ('EffectsManager', r'^QT(CreateStandardParameterDialog|DismissStandardParameterDialog|IsStandardParameterDialogEvent|StandardParameterDialogDoAction|GetEffectSpeed|GetEffectsList)$'),
 ('MediaHandlerManager', r'^(Media(?!TimeToSampleNum)|TextMedia|FlashMedia|VideoMedia|MovieMedia|SpriteMedia)'),
 ('ComponentManager', r'^(OpenAComponent|CloseComponent|FindNextComponent|CountComponents|GetComponentInfo|GetDataHandler|GetMovieImporterForDataRef)$|^Movie(Import|Export)|^TC|^XMLParse|^GetMediaHandlerDescription$|^GetMediaDataHandlerDescription$'),
 ('PlatformManager', r'^(CreatePortAssociation|DestroyPortAssociation|DestroyPortAssociations|GetHWND|DestroyHWnd|GetNativeWindowPort|DoSecurityInit|InstallEventLoopTimer|RemoveEventLoopTimer|SetEventLoopTimerNextFireTime|GetMainEventLoop|_UseMainRunLoopForCurrentThread|NewMethodClosure|DisposeMethodClosure|SetJavaThreadCallbackProc|initGlobalLock|initGlobalOffScreen|loadNativeLibrary|unloadNativeLibrary|Gestalt|GetQuickTimePreference|SetQuickTimePreference|GetNextEvent|DrawMenuBar|RemoveContext|RemoveNewContext|getDrawingSurface|getDrawingSurfaceHWnd|lockDrawingSurface|unlockDrawingSurface|freeDrawingSurface|getCompHWnd|getBounds|setFullScreen|setMovie|setMovieController|createNSViewNative|createImageNSViewNative|createSequenceGrabberNSView|getMovieController|removeMovie|BeginFullScreen|EndFullScreen|QTRegisterAccessKey|QTUnregisterAccessKey|QTGetAccessKeys|QTRestrictionsGetInfo|QTRestrictionsGetItem|QTGetMovieRestrictions|QTGetSupportedRestrictions)$'),
 ('QuickDrawManager', r'Rgn|Rect$|Region|GWorld$|^(GetGWorld|SetGWorld|NewGWorld|QTNewGWorld|NewImageGWorld|DisposeGWorld|GetGWorldPixMap|LockPixels|UnlockPixels|GetPixBaseAddr|GetPixBounds|GetPort|SetPort|MacSetPort|GetPortBounds|IsPortOffscreen|GetGDevice|SetGDevice|GetMainDevice|GetMaxDevice|GetNextDevice|GetDeviceList|InitGDevice|SetDeviceAttribute|TestDeviceAttribute|GDGetScale|GDHasScale|GDSetScale|DisposeCTable|DisposeMatte|KillPicture|DrawPicture|DrawTrimmedPicture|OpenCPicture|ClosePicture|OpenPoly|ClosePoly|KillPoly|OffsetPoly|FramePoly|PaintPoly|ErasePoly|InvertPoly|QDError|QDFlushPortBuffer|GetCPixel|SetCPixel|RGBForeColor|RGBBackColor|GetForeColor|GetBackColor|PenNormal|PenSize|Move|MoveTo|Line|LineTo|MacLineTo|TextFace|TextFont|TextMode|TextSize|TextWidth|CharWidth|DrawChar|DrawText|MacDrawText|StdText|StdTxMeas|MeasureText|GetFNum|GetFontInfo|GetFontName|BeginUpdate|EndUpdate|SetClip|GetClip|ClipRect|InvalRect|GetPortClipRegion|GetPortVisibleRegion|FrameArc|PaintArc|EraseArc|InvertArc|FrameOval|PaintOval|EraseOval|InvertOval|FrameRoundRect|PaintRoundRect|EraseRoundRect|InvertRoundRect|ITextAddString|ITextGetString|ITextRemoveString)$'),
]
PLAT = [p for f, p in RULES if f == 'PlatformManager'][0]
def route(name):
    for fac, pat in RULES:
        if fac == 'PlatformManager0':
            if re.search(PLAT, name): return 'PlatformManager'
            continue
        if re.search(pat, name): return fac
    return 'MovieToolbox'

DEFAULTS = {'void': None}
def main():
    ns = scan()
    sigs = collections.OrderedDict()   # (fac,name,ptypes) -> dict
    for n in sorted(ns, key=lambda n: (n['name'], n['file'])):
        fac = route(n['name'])
        key = (fac, n['name'], tuple(n['ptypes']))
        if key not in sigs:
            sigs[key] = dict(ret=collections.Counter(), pnames=n['pnames'], files=set())
        sigs[key]['ret'][n['ret']] += 1
        sigs[key]['files'].add(n['file'])
    fret = {}
    for k, v in sigs.items():
        fret[k] = v['ret'].most_common(1)[0][0]
    # facades
    os.makedirs(OUT, exist_ok=True)
    byfac = collections.defaultdict(list)
    for k, v in sigs.items(): byfac[k[0]].append(k)
    for fac, keys in byfac.items():
        lines = []
        lines.append('/*\n * Copyright (c) 2026 by Naohide Sano, All rights reserved.\n *\n * Programmed by Naohide Sano\n */\n')
        lines.append('package %s;\n' % PKG)
        lines.append('import java.awt.Component;\n')
        lines.append('\n/**\n * %s.\n * <p>\n * Java replacement for the QuickTime native functions of this subsystem.\n * Functions that have no modern equivalent throw {@link UnsupportedToolboxException}.\n */' % fac)
        lines.append('public final class %s {\n\n    private %s() {\n    }\n' % (fac, fac))
        for k in sorted(keys, key=lambda k: (k[1], k[2])):
            _, name, ptypes = k
            v = sigs[k]
            params = ', '.join('%s %s' % (t, p) for t, p in zip(ptypes, v['pnames']))
            lines.append('    /** used by %s */' % ', '.join(sorted(f.rsplit('/',1)[-1][:-5] for f in v['files'])))
            lines.append('    public static %s %s(%s) {' % (fret[k], name, params))
            lines.append('        throw new UnsupportedToolboxException("%s");' % name)
            lines.append('    }\n')
        lines.append('}\n')
        open(os.path.join(OUT, fac + '.java'), 'w').write('\n'.join(lines))
    # rewrite sources
    byfile = collections.defaultdict(list)
    for n in ns: byfile[n['file']].append(n)
    for f, lst in byfile.items():
        p = os.path.join(ROOT, f)
        src = open(p, encoding='utf-8').read()
        used = set()
        for n in sorted(lst, key=lambda n: -n['start']):
            fac = route(n['name']); used.add(fac)
            k = (fac, n['name'], tuple(n['ptypes']))
            mods = ' '.join(n['mods'])
            params = ', '.join('%s %s' % (t, pn) for t, pn in zip(n['ptypes'], n['pnames']))
            call = '%s.%s(%s)' % (fac, n['name'], ', '.join(n['pnames']))
            if n['ret'] == 'void':
                body = call + ';'
            elif n['ret'] != fret[k]:
                body = 'return (%s) %s;' % (n['ret'], call)
            else:
                body = 'return ' + call + ';'
            # indentation of decl
            ls = src.rfind('\n', 0, n['start']) + 1
            indent = re.match(r'\s*', src[ls:n['start']]).group(0)
            text = '%s %s %s(%s) {\n%s    %s\n%s}' % (mods, n['ret'], n['name'], params, indent, body, indent)
            src = src[:n['start']] + text.lstrip() + src[n['end']:]
        imports = ''.join('import %s.%s;\n' % (PKG, fac) for fac in sorted(used))
        lines = src.split('\n')
        imps = [i for i, l in enumerate(lines) if l.startswith('import ')]
        newimps = ['import %s.%s;' % (PKG, fac) for fac in sorted(used)]
        if imps:
            qt = [i for i in imps if lines[i] >= 'import quicktime.internal']
            pos = qt[0] if qt else imps[-1] + 1
            lines[pos:pos] = newimps
        else:
            pk = [i for i, l in enumerate(lines) if l.startswith('package ')][0]
            lines[pk + 1:pk + 1] = [''] + newimps
        src = '\n'.join(lines)
        open(p, 'w', encoding='utf-8').write(src)
    c = collections.Counter(k[0] for k in sigs)
    print(sorted(c.items(), key=lambda x: -x[1]))
main()
