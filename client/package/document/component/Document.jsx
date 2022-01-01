import React, { useState, useRef, useContext, createContext } from "react";
import { Document, Page, pdfjs } from "react-pdf";
import dynamic from 'next/dynamic';
import PreviewModal from "../../../package/document/component/PreviewModal";
pdfjs.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.min.js`;

const FileViewer = dynamic(() => import('react-file-viewer'), {
  ssr: false
});
const DocumentFile = ({ file }) => {
  console.log("file-sss", file);
  const [document, setDocument] = useState(null);
  const pdfRef = useRef(null);

  const [numPages, setNumPages] = useState(null);
  const [pageNumber, setPageNumber] = useState(1);

  const onDocumentLoadSuccess = ({ numPages }) => {
    setNumPages(numPages);
  };

  return (
    <>
      {file.length > 0 ? (
        <div id="list-doc" className="grid grid-cols-3 gap-5">
          {file.map((data, index) => {
            const urlFile = URL.createObjectURL(
              new Blob([new Uint8Array(data.contents)], {
                type: "application/pdf",
              })
            );
            console.log("urlFile", urlFile);
            return (
              <div key={index} className="p-3 bg-white">
                <div
                  // ref={pdfRef}
                  className="w-full view-pdf-s"
                  // onClick={() => setDocument(file)}
                >
                  {/* {pdfRef.current && (
                    <Document
                      file={urlFile}
                      onLoadSuccess={onDocumentLoadSuccess}
                    >
                      <Page
                        pageNumber={pageNumber}
                        width={pdfRef.current.clientWidth}
                      />
                    </Document>
                  )} */}
                  {/* <Pdf file={URL.createObjectURL(
                      new Blob([new Uint8Array(data.contents)], {
                        type: "application/pdf",
                      }))} page={1} /> */}
                  <FileViewer
                    fileType="pdf"
                    filePath={URL.createObjectURL(
                      new Blob([new Uint8Array(data.contents)], {
                        type: "application/pdf",
                      }))}
                    pageNumber={1}
                    />
                </div>
                <div className="mt-2">{data.title}</div>
                <div className="mt-2">{data.mark}</div>
              </div>
            );
          })}
        </div>
      ) : (
        <div>Loading data..</div>
      )}

      {document && (
        <PreviewModal toggle={() => setDocument(null)} document={document} />
      )}
    </>
  );
};

export default DocumentFile;
