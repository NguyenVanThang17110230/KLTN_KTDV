import React, { useState, useRef, useContext, createContext } from "react";
import { Document, Page, pdfjs } from "react-pdf";

import PreviewModal from "../../../package/document/component/PreviewModal";
pdfjs.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.min.js`;

const DocumentFile = () => {
  const [document, setDocument] = useState(null);
  const pdfRef = useRef(null);

  const [numPages, setNumPages] = useState(null);
  const [pageNumber, setPageNumber] = useState(1);

  function onDocumentLoadSuccess({ numPages }) {
    setNumPages(numPages);
  }

  return (
    <>
      <div id='list-doc' className="grid grid-cols-3 gap-5">
        <div className="p-3 bg-white">
          <div
            ref={pdfRef}
            className="w-full"
            onClick={() => setDocument("./Dethi_QuaTrinh01.pdf")}
          >
            {pdfRef.current && (
              <Document
                file="./Dethi_QuaTrinh01.pdf"
                onLoadSuccess={onDocumentLoadSuccess}
              >
                <Page
                  pageNumber={pageNumber}
                  width={pdfRef.current.clientWidth}
                />
              </Document>
            )}
          </div>
          <div className="mt-2">title</div>
          <div className="mt-2">author</div>
        </div>
        <div className="p-3 bg-white">
          <div
            ref={pdfRef}
            className="w-full"
            onClick={() => setDocument(document)}
          >
            {pdfRef.current && (
              <Document
                file="./CV_NguyenVanThang.pdf"
                onLoadSuccess={onDocumentLoadSuccess}
              >
                <Page
                  pageNumber={pageNumber}
                  width={pdfRef.current.clientWidth}
                />
              </Document>
            )}
          </div>
          <div className="mt-2">title</div>
          <div className="mt-2">author</div>
        </div>
        <div className="p-3 bg-white">
          <div
            ref={pdfRef}
            className="w-full"
            onClick={() => setDocument(document)}
          >
            {pdfRef.current && (
              <Document
                file="./Dethi_QuaTrinh01.pdf"
                onLoadSuccess={onDocumentLoadSuccess}
              >
                <Page
                  pageNumber={pageNumber}
                  width={pdfRef.current.clientWidth}
                />
              </Document>
            )}
          </div>
          <div className="mt-2">title</div>
          <div className="mt-2">author</div>
        </div>
        <div className="p-3 bg-white">
          <div
            ref={pdfRef}
            className="w-full"
            onClick={() => setDocument("./Dethi_QuaTrinh01.pdf")}
          >
            {pdfRef.current && (
              <Document
                file="./Dethi_QuaTrinh01.pdf"
                onLoadSuccess={onDocumentLoadSuccess}
              >
                <Page
                  pageNumber={pageNumber}
                  width={pdfRef.current.clientWidth}
                />
              </Document>
            )}
          </div>
          <div className="mt-2">title</div>
          <div className="mt-2">author</div>
        </div>
        <div className="p-3 bg-white">
          <div
            ref={pdfRef}
            className="w-full"
            onClick={() => setDocument(document)}
          >
            {pdfRef.current && (
              <Document
                file="./CV_NguyenVanThang.pdf"
                onLoadSuccess={onDocumentLoadSuccess}
              >
                <Page
                  pageNumber={pageNumber}
                  width={pdfRef.current.clientWidth}
                />
              </Document>
            )}
          </div>
          <div className="mt-2">title</div>
          <div className="mt-2">author</div>
        </div>
        <div className="p-3 bg-white">
          <div
            ref={pdfRef}
            className="w-full"
            onClick={() => setDocument(document)}
          >
            {pdfRef.current && (
              <Document
                file="./Dethi_QuaTrinh01.pdf"
                onLoadSuccess={onDocumentLoadSuccess}
              >
                <Page
                  pageNumber={pageNumber}
                  width={pdfRef.current.clientWidth}
                />
              </Document>
            )}
          </div>
          <div className="mt-2">title</div>
          <div className="mt-2">author</div>
        </div>
        {/* <div className="p-3 bg-white">
        <div>
          <Document
            file="./CV_NguyenVanThang.pdf"
            onLoadSuccess={onDocumentLoadSuccess}
          >
            <Page pageNumber={pageNumber} width={pdfRef.current.clientWidth}/>
          </Document>
        </div>
        <div className="mt-2">title</div>
        <div className="mt-2">author</div>
      </div>
      <div className="p-3 bg-white">
        <div>
          <Document
            file="./CV_NguyenVanThang.pdf"
            onLoadSuccess={onDocumentLoadSuccess}
          >
            <Page pageNumber={pageNumber} />
          </Document>
        </div>
        <div className="mt-2">title</div>
        <div className="mt-2">author</div>
      </div>
      <div className="p-3 bg-white">
        <div>
          <Document
            file="./Dethi_QuaTrinh01.pdf"
            onLoadSuccess={onDocumentLoadSuccess}
          >
            <Page pageNumber={pageNumber} />
          </Document>
        </div>
        <div className="mt-2">title</div>
        <div className="mt-2">author</div>
      </div> */}
      </div>
      {document && (
        <PreviewModal toggle={() => setDocument(null)} document={document} />
      )}
    </>
  );
};

export default DocumentFile;
